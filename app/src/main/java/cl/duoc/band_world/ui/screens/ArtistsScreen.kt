package cl.duoc.band_world.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.User
import cl.duoc.band_world.ui.components.CustomInput
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes


@Composable
fun ArtistsScreen(
    artists         : List<Artist>?,
    title           : String,
    user            : User?,
    navController   : NavController
) {

    var searchText by remember { mutableStateOf("") }

    val artists_ = artists?.filter { it.name.contains(searchText, ignoreCase = true) }


    GenericListScreen(
        title       = title,
        items       = artists_,
        circleImage = true,
        getDataItem = {
            GeneralCardItem(
                id = it.id,
                name = it.name,
                image = it.image,
                isFavorite = it.isFavorite
            )
        },
        userId      = user?.id,
        favoriteType= FavoriteType.ARTIST,
        onClick     = {
            navController.navigate("${Routes.SINGLE_ARTIST.route}/${it.id}")
        },
        composable  = {
            CustomInput(
                value           = searchText,
                placeholder     = "Ingrese para buscar...",
                icon            = Icons.Default.Search,
                onValueChange   = {
                    searchText = it
                },
            )
            addHeight()
        },
    )
}

