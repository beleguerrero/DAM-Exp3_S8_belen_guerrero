package cl.duoc.band_world.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.ui.components.BaseBotonFav
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.GeneralCard
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.ImageSingleContent
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.TypeButtonFav
import cl.duoc.band_world.ui.components.UserFav
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes
import kotlinx.coroutines.launch
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun SingleGenreScreen(
    navController   : NavController,
    genre           : Genre,
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

    var bands by remember { mutableStateOf<List<Band>>(emptyList()) }
    LaunchedEffect(Unit) {
        bands = bandFunctions.getBandsByGenre(context, genre.id, user?.id)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            ImageSingleContent(
                image       = null,
                hideImage   = true,
                title       = genre.name,
                extraText   = if(!bands.isNullOrEmpty()) {
                    "${bands.size} Bandas"
                } else "Sin bandas"
            )
        }

        // BOTÓN FAVORITO
        if(user != null) {
            item {
                addHeight(3.dp)
                var isFavorite by remember {
                    mutableStateOf(genre.isFavorite)
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
                                itemId = genre.id,
                                isFavorite = isFavorite,
                                type = FavoriteType.GENRE
                            )
                            isFavorite = !isFavorite
                        }
                    }
                )
            }
        }

        item {
//            addHeight(30.dp)
            Text(
                modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                text = "Bandas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(bands.chunked(2)) { rowBands ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = PADDING_HORIZONTAL),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowBands.forEach { band ->
                    val userFav = if(user != null) {
                        UserFav(
                            userId  = user.id,
                            favId   = band.id
                        )
                    } else null

                    GeneralCard (
                        modifier    = Modifier.weight(1f),
                        getDataItem = GeneralCardItem(
                            id = band.id,
                            name = band.name,
                            image = band.image,
                            isFavorite = band.isFavorite
                        ),
                        userFav = userFav,
                        onClick     = {
                            navController.navigate("${Routes.SINGLE_BAND.route}/${band.id}")
                        }
                    )
                }
            }
        }
    }
}
