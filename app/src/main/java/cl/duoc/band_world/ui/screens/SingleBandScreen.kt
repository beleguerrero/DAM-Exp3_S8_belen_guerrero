package cl.duoc.band_world.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.R
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.Member
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.UserFunctions
import cl.duoc.band_world.ui.components.BaseBotonFav
import cl.duoc.band_world.ui.components.CardNoData
import cl.duoc.band_world.ui.components.CustomScrollableTab
import cl.duoc.band_world.ui.components.DescriptionCard
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.GeneralCard
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.ImageSingleContent
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.TypeButtonFav
import cl.duoc.band_world.ui.components.UserFav
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.components.addWidth
import cl.duoc.band_world.ui.navigation.Routes
import kotlinx.coroutines.launch


//val Band.formateaGeneros: String get() = genres.joinToString(" • ") { it.name }

val Band.formateaDetalles: String get() = "${country.emoji} • ${yearFounded} ${yearDisbanded?.let { " - $it" } ?: ""} • ${status.name}"
@Composable
fun SingleBandScreen(
    navController   : NavController,
    band            : Band,
    user            : User?
) {

    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val favoriteFunctions = remember {
        FavoriteFunctions(database.bandDao(),database.userDao())
    }

    val scope = rememberCoroutineScope()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Actuales", "Ex integrantes")


    val integrantesActuales = band.members.filter { member -> member.endYear == null }
    val exIntegrantes       = band.members.filter { member -> member.endYear != null }
    var descriptionExpanded by remember { mutableStateOf(false) }

    val genreScrollState    = rememberScrollState()
    val memberScrollState   = rememberScrollState()
    val exMemberScrollState = rememberScrollState()
    val albumScrollState    = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp),
    ) {

        // IMAGEN BANDA
        item {
            ImageSingleContent(
                image       = band.image,
                title       = band.name,
                extraText   = band.formateaDetalles
            )
        }


        // BOTÓN FAVORITO
        if(user != null) {
            item {
                addHeight()
                var isFavorite by remember {
                    mutableStateOf(band.isFavorite)
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
                                itemId = band.id,
                                isFavorite = isFavorite,
                                type = FavoriteType.BAND
                            )
                            isFavorite = !isFavorite
                        }
                    }
                )
            }
        }


        // GENEROS
        item {
            addHeight()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(genreScrollState)
                    .padding(horizontal = PADDING_HORIZONTAL),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                band.genres.forEach { genre ->
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("${Routes.SINGLE_GENRE.route}/${genre.id}")
                            }
                    ) {
                        Text(
                            text = genre.name,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }


        // DESCRIPCIÓN
        if(!band.description.isNullOrBlank()) {
            item {
                addHeight()
                DescriptionCard(
                    modifier            = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                    description         = band.description,
                    expanded            = descriptionExpanded,
                    onExpandedChange    = { descriptionExpanded = !descriptionExpanded }
                )
            }
        }


        // INTEGRANTES
        item {
            addHeight(35.dp)
            Text(
                modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                text = "Integrantes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            addHeight(8.dp)

            CustomScrollableTab(
                modifier    = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                selectedTab = selectedTab,
                listTabs    = tabs,
                onSelected  = { index ->
                    selectedTab = index
                }
            )
        }

        when (selectedTab) {
            // ACTUALES
            0 -> item {
                addHeight()
                getMembersTabContent(
                    listaIntegrantes    = integrantesActuales,
                    scrollState         = memberScrollState,
                    userId              = user?.id,
                    navController       = navController
                )
            }

            // EX INTEGRANTES
            1 -> item {
                addHeight()
                if(exIntegrantes.isNotEmpty()) {
                    getMembersTabContent(
                        listaIntegrantes    = exIntegrantes,
                        scrollState         = exMemberScrollState,
                        userId              = user?.id,
                        navController       = navController
                    )
                } else {
                    CardNoData(
                        modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                        title = "No tiene Ex integrantes"
                    )
                }
            }
        }

        // ÁLBUMES
        item {
            addHeight(30.dp)
            Text(
                modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                text = "Discografía",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        if(!band.albums.isNullOrEmpty()) {
            item {
                addHeight(10.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(albumScrollState)
                        .padding(horizontal = PADDING_HORIZONTAL),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    band.albums.take(5).forEach { album ->
                        val userFav = if(user != null) {
                            UserFav(
                                userId  = user.id,
                                favId   = album.id
                            )
                        } else null

                        GeneralCard(
                            modifier    = Modifier.width(150.dp),
                            userFav     = userFav,
                            getDataItem = GeneralCardItem(
                                id = album.id,
                                name = album.name,
                                image = album.image,
                                secondLine = "Álbum • ${album.releaseDate.year}",
                                isFavorite = album.isFavorite
                            ),
//                            favoriteType= FavoriteType.ALBUM,
                            onClick     = {
                                navController.navigate("${Routes.SINGLE_ALBUM.route}/${album.id}")
                            }
                        )
                    }
                }
            }
        } else {
            item {
                addHeight(5.dp)
                CardNoData(
                    modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL),
                    title = "No tiene álbumes :("
                )
            }
        }
    }
}

@Composable
fun getMembersTabContent(
    listaIntegrantes    : List<Member>,
    scrollState         : ScrollState,
    userId              : String? = null,
    navController       : NavController,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = PADDING_HORIZONTAL),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        listaIntegrantes.forEach { member ->
            val userFav = if(userId != null) {
                UserFav(
                    userId  = userId,
                    favId   = member.artist.id
                )
            } else null
            GeneralCard(
                modifier    = Modifier.width(150.dp),
                circleImage = true,
                userFav     = userFav,
                getDataItem = GeneralCardItem(
                    id = member.artist.id,
                    name = member.artist.name,
                    image = member.artist.image,
                    secondLine = "${member.startYear}${member.endYear?.let { " - $it" } ?: "" }",
                    isFavorite = member.artist.isFavorite
                ),
//                favoriteType= FavoriteType.ARTIST,
                onClick     = {
                    navController.navigate("${Routes.SINGLE_ARTIST.route}/${member.artist.id}")
                },
                thirdLine   = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        member.roles.forEach { role ->
                            Image(
                                painter = painterResource(
                                    id = role.image ?: R.drawable.sin_imagen
                                ),
                                contentDescription = role.name,
                                modifier = Modifier
                                    .width(25.dp)
                                    .height(30.dp),
                                contentScale = ContentScale.Fit
                            )
                            addWidth(5.dp)
                        }
                    }
                },
            )
        }
    }
}
