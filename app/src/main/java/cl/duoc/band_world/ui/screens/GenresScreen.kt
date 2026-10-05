package cl.duoc.band_world.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.User
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.navigation.Routes


@Composable
fun GenresScreen(
    genres          : List<Genre>?,
    title           : String,
    user            : User?,
    navController   : NavController
) {
    GenericListScreen(
        title       = title,
        items       = genres,
        getDataItem = {
            GeneralCardItem(
                id = it.id,
                name = it.name,
                image = null,
                isFavorite = it.isFavorite
            )
        },
        userId      = user?.id,
        favoriteType= FavoriteType.GENRE,
        onClick     = {
            navController.navigate("${Routes.SINGLE_GENRE}/${it.id}")
        },
    )
}

