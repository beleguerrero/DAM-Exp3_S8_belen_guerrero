package cl.duoc.band_world.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.ui.components.addHeight


//enum class Routes(
//    val route: String
//) {
//    HOME("home"),
//    BANDS("bands"),
//    ARTISTS("artists"),
//    FAVORITES("favorites"),
//
//}
//enum class Routes(
//    val route: String,
//    val label: String? = null,
//    val icon: ImageVector? = null,
//    val contentDescription: String? = null,
//    val isNavigationBar: Boolean = false
//) {
//    HOME(
//        route = "home",
//        label = "Inicio",
//        icon = Icons.Default.Home,
//        contentDescription = "Home",
//        isNavigationBar = true
//    ),
//
//    BANDS(
//        route = "bands",
//        label = "Bandas",
//        icon = Icons.Default.Stars,
//        contentDescription = "Bandas",
//        isNavigationBar = true
//    ),
//
//    FAVORITES(
//        route = "favorites",
//        label = "Favoritos",
//        icon = Icons.Default.Favorite,
//        contentDescription = "Favoritos",
//        isNavigationBar = true
//    ),
//
//    PROFILE(
//        route = "profile",
//        label = "Mi perfil",
//        icon = Icons.Default.Person,
//        contentDescription = "Mi perfil",
//        isNavigationBar = true
//    ),
//
//    ARTISTS(
//        route = "artists",
//        label = "Artistas",
//        icon = Icons.Default.Stars,
//        contentDescription = "Artistas"
//    ),
//
//    RESET(route = "reset"),
//    REGISTER(route = "register"),
//    LOGIN(route = "login"),
//    SINGLE_ARTIST(route = "artist"),
//    SINGLE_BAND(route = "band"),
//    SINGLE_ALBUM(route = "album"),
//    SINGLE_GENRE(route = "genre"),
//}

@Composable
fun AppBottomBar(
    selectedDestination: Int,
    onSelected: (Int) -> Unit,
    navController: NavController
) {
    NavigationBar(
        modifier = Modifier
            .shadow(elevation = 12.dp),
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Routes.entries.filter { d ->
            d.isNavigationBar == true
        }.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(route = destination.route)
                    onSelected(index)
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if(destination.icon != null) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.contentDescription
                            )
                            addHeight(3.dp)
                        }

                        if(destination.label != null) {
                            Text(
                                text = destination.label,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                label = {
                    var selectedPointColor = Color.Transparent
                    if(selectedDestination == index) {
                        selectedPointColor = MaterialTheme.colorScheme.onSurface
                    }
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .height(5.dp)
                            .background(
                                color = selectedPointColor,
                                shape = RoundedCornerShape(5.dp)
                            ).
                            shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(3.dp),
                                ambientColor = selectedPointColor,
                                spotColor = selectedPointColor
                            )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.outline,
                    unselectedTextColor = MaterialTheme.colorScheme.outline,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

