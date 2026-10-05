package cl.duoc.band_world.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.Album
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.ui.components.ImageSingleContent
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.SongCard
import cl.duoc.band_world.ui.components.addHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cl.duoc.band_world.R
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.ui.components.BaseBotonFav
import cl.duoc.band_world.ui.components.DescriptionCard
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.SongCardItem
import cl.duoc.band_world.ui.components.TypeButtonFav
import cl.duoc.band_world.ui.components.UserFav
import cl.duoc.band_world.ui.components.addWidth
import cl.duoc.band_world.ui.navigation.Routes
import kotlinx.coroutines.launch


@Composable
fun SingleAlbumScreen(
    navController   : NavController,
    album           : Album,
    user            : User?
) {
    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val bandFunctions = remember {
        BandFunctions(database.bandDao())
    }
    val favoriteFunctions = remember {
        FavoriteFunctions(database.bandDao(),database.userDao())
    }

    val scope = rememberCoroutineScope()

    val albumId = album.id
    var band        by remember { mutableStateOf<Band?>(null) }
    var bandId      by remember { mutableStateOf("") }
    var bandName    by remember { mutableStateOf("") }
    var bandImage   by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        band        = bandFunctions.getBandByAlbum(context, albumId, user?.id)
        bandId      = band!!.id
        bandName    = band!!.name
        bandImage   = band?.image
    }

    var descriptionExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp),
    ) {

        item {
            ImageSingleContent(
                image       = album.image,
                title       = album.name,
                extraText   = "Álbum • ${album.releaseDate.year} • ${album.songs.size} canciones"
            )
        }

        item {
            addHeight()
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        navController.navigate("${Routes.SINGLE_BAND.route}/${bandId}")
                    }
                ) {
                    Image(
                        painter = painterResource(
                            id = bandImage ?: R.drawable.sin_imagen
                        ),
                        contentDescription = bandName,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )

                    addWidth(8.dp)

                    Text(
                        text = bandName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }


        // BOTÓN FAVORITO
        if(user != null) {
            item {
                addHeight()
                var isFavorite by remember {
                    mutableStateOf(album.isFavorite)
                }
                val typeButtonFav = when(isFavorite) {
                    true -> TypeButtonFav.SIGUIENDO
                    false -> TypeButtonFav.SEGUIR
                }
                BaseBotonFav(
                    modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                    type = typeButtonFav,
                    onClick = {
                        scope.launch {
                            favoriteFunctions.toggleFavorite(
                                userId = user.id,
                                itemId = album.id,
                                isFavorite = isFavorite,
                                type = FavoriteType.ALBUM
                            )
                            isFavorite = !isFavorite
                        }
                    }
                )
            }
        }

        // DESCRIPCIÓN
        if(!album.description.isNullOrBlank()) {
            item {
                addHeight()
                DescriptionCard(
                    modifier            = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                    description         = album.description,
                    expanded            = descriptionExpanded,
                    onExpandedChange    = { descriptionExpanded = !descriptionExpanded }
                )
            }
        }

        // CANCIONES
        item {
            addHeight(30.dp)
            Text(
                modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                text = "Canciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            addHeight(8.dp)
        }

        items(album.songs) { song ->
            val userFav = if(user != null) {
                UserFav(
                    userId  = user.id,
                    favId   = song.id
                )
            } else null

            SongCard(
                getDataItem = SongCardItem(
                    id = song.id,
                    name = song.name,
                    number = song.number.toString(),
                    isFavorite = song.isFavorite
                ),
                userFav     = userFav,
                modifier    = Modifier.padding(horizontal = PADDING_HORIZONTAL)
            )
            addHeight(5.dp)
        }
    }
}

