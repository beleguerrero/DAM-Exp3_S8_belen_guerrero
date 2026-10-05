package cl.duoc.band_world.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.UserFunctions
import cl.duoc.band_world.ui.components.CustomInput
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.FilterButton
import cl.duoc.band_world.ui.components.FilterType
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.ListaFiltro
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes

@Composable
fun BandsScreen(
    bands           : List<Band>?,
    title           : String,
    user            : User?,
    navController   : NavController
) {

    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val bandFunctions = remember {
        BandFunctions(database.bandDao())
    }

    var searchText by remember { mutableStateOf("") }

    var filterIncludes by remember { mutableStateOf<List<ListaFiltro>>(emptyList()) }
    var filterExcludes by remember { mutableStateOf<List<ListaFiltro>>(emptyList()) }

//     val bandFunctions = BandFunctions()
     val bands_ = bandFunctions.getBandsFilter(bands = bands,includes = filterIncludes, excludes = filterExcludes)?.filter { it.name.contains(searchText, ignoreCase = true) }

    GenericListScreen(
        title       = title,
        items       = bands_,
        getDataItem = {
            GeneralCardItem(
                id = it.id,
                name = it.name,
                image = it.image,
                isFavorite = it.isFavorite
            )
        },
        userId      = user?.id,
        favoriteType= FavoriteType.BAND,
        onClick     = {
            navController.navigate("${Routes.SINGLE_BAND.route}/${it.id}")
        },
        composable  = {
            CustomInput(
                value           = searchText,
                placeholder     = "Ingrese para buscar...",
                icon            = Icons.Default.Search,
                onValueChange   = {
                    searchText = it
                },
                endButton       = {
                    FilterButton(
                        bandFunctions   = bandFunctions,
                        filterOption    = FilterType.BANDS,
                        includes        = filterIncludes,
                        excludes        = filterExcludes,
                        onChange        = { newIncludes, newExcludes ->
                            filterIncludes = newIncludes
                            filterExcludes = newExcludes
                        }
                    )
                }
            )
            addHeight()
        }
    )

}

