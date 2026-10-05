package cl.duoc.band_world.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duoc.band_world.data.Album
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.SessionFunctions
import cl.duoc.band_world.functions.UserFunctions
import cl.duoc.band_world.ui.screens.ArtistsScreen
import cl.duoc.band_world.ui.screens.BandsScreen
import cl.duoc.band_world.ui.screens.FavoriteScreen
import cl.duoc.band_world.ui.screens.GenresScreen
import cl.duoc.band_world.ui.screens.HomeScreen
import cl.duoc.band_world.ui.screens.LoginScreen
import cl.duoc.band_world.ui.screens.MyProfileScreen
import cl.duoc.band_world.ui.screens.RegisterScreen
import cl.duoc.band_world.ui.screens.ResetScreen
import cl.duoc.band_world.ui.screens.SingleAlbumScreen
import cl.duoc.band_world.ui.screens.SingleArtistScreen
import cl.duoc.band_world.ui.screens.SingleBandScreen
import cl.duoc.band_world.ui.screens.SingleGenreScreen


enum class Routes(
    val route               : String,
    val label               : String? = null,
    val icon                : ImageVector? = null,
    val contentDescription  : String? = null,
    val isNavigationBar     : Boolean = false
) {
    HOME(
        route               = "home",
        label               = "Inicio",
        icon                = Icons.Default.Home,
        contentDescription  = "Home",
        isNavigationBar     = true
    ),

    BANDS(
        route               = "bands",
        label               = "Bandas",
        icon                = Icons.Default.Stars,
        contentDescription  = "Bandas",
    ),

    FAVORITES(
        route               = "favorites",
        label               = "Favoritos",
        icon                = Icons.Default.Favorite,
        contentDescription  = "Favoritos",
        isNavigationBar     = true
    ),

    PROFILE(
        route               = "profile",
        label               = "Mi perfil",
        icon                = Icons.Default.Person,
        contentDescription  = "Mi perfil",
        isNavigationBar     = true
    ),

    ARTISTS(
        route               = "artists",
        label               = "Artistas",
        icon                = Icons.Default.Stars,
        contentDescription  = "Artistas"
    ),

    RESET(route = "reset"),
    REGISTER(route = "register"),
    LOGIN(route = "login"),
    SINGLE_ARTIST(route = "artist"),
    SINGLE_BAND(route = "band"),
    SINGLE_ALBUM(route = "album"),

    GENRES(route = "genres"),
    SINGLE_GENRE(route = "genre"),
}

@Composable
fun AppNavHost(navController: NavHostController, startDestination: Routes, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val bandFunctions = remember {
        BandFunctions(database.bandDao())
    }
    val favoriteFunctions = remember {
        FavoriteFunctions(database.bandDao(), database.userDao())
    }
    val userFunctions = remember {
        UserFunctions(database.userDao())
    }

    val argFavorites = "favorites"
    val argAll = "all"


    var userId          by remember { mutableStateOf<String?>(null) }
    var usuarioActual   by remember { mutableStateOf<User?>(null) }

    var bands           by remember { mutableStateOf<List<Band>>(emptyList()) }
    var favBands        by remember { mutableStateOf<List<Band>?>(emptyList()) }

    var artists         by remember { mutableStateOf<List<Artist>>(emptyList()) }
    var favArtists      by remember { mutableStateOf<List<Artist>?>(emptyList()) }

    var genres          by remember { mutableStateOf<List<Genre>>(emptyList()) }
    var favGenres       by remember { mutableStateOf<List<Genre>?>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            userId          = SessionFunctions.getUserId(context)
            usuarioActual   = userId?.let { userFunctions.getUserById(it) }

            bands           = bandFunctions.getAllBands(context, usuarioActual?.id)
            favBands        = usuarioActual?.let { favoriteFunctions.getUserFavoriteBands(context, it.id) }

            artists         = bandFunctions.getAllArtists(context, usuarioActual?.id)
            favArtists      = usuarioActual?.let { favoriteFunctions.getUserFavoriteArtists(context, it.id) }

            genres          = bandFunctions.getAllGenres(usuarioActual?.id)
            favGenres       = usuarioActual?.let { favoriteFunctions.getUserFavoriteGenres(it.id) }
        } catch (e: Exception) {
            println("Error: ${e}")
        }
    }

    NavHost(
        navController       = navController,
        startDestination    = startDestination.route,
        modifier            = modifier
    ) {
        Routes.entries.filter { d ->
            d.isNavigationBar == true
        }.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Routes.HOME -> HomeScreen(
                        navController   = navController,
                        user            = usuarioActual
                    )
                    Routes.FAVORITES -> FavoriteScreen(
                        navController   = navController,
                        user            = usuarioActual
                    )
                    Routes.PROFILE -> MyProfileScreen(
                        navController   = navController,
                        user            = usuarioActual,
                        onLogin         = { user ->
                            usuarioActual = user
                        },
                        onUserUpdated   = { user ->
                            usuarioActual = user
                        },
                        onLogout        = {
                            SessionFunctions.clear(context)
                            usuarioActual = null
                        }
                    )
                    else -> {}
                }
            }
        }

        composable("${Routes.BANDS.route}/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")

            BandsScreen(
                bands = when(type) {
                    argFavorites -> favBands
                    argAll -> bands
                    else -> emptyList()
                },
                title = when(type) {
                    argFavorites -> "Mis bandas"
                    argAll -> "Todas las bandas"
                    else -> "Bandas"
                },
                navController = navController,
                user = usuarioActual
            )
        }



        composable("${Routes.GENRES.route}/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")

            GenresScreen(
                genres = when(type) {
                    argFavorites -> favGenres
                    argAll -> genres
                    else -> emptyList()
                },
                title = when(type) {
                    argFavorites -> "Mis Géneros"
                    argAll -> "Todos los géneros"
                    else -> "Géneros"
                },
                navController = navController,
                user = usuarioActual
            )
        }

        composable(Routes.LOGIN.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { user ->
                    usuarioActual = user
                    navController.navigate(Routes.PROFILE.route)
                }
            )
        }

        composable(Routes.REGISTER.route) {
            RegisterScreen(
                navController = navController
            )
        }

        composable(Routes.RESET.route) {
            ResetScreen(
                navController = navController
            )
        }

        composable("${Routes.SINGLE_BAND.route}/{id}") { backStackEntry ->
            val context = LocalContext.current
            var band by remember { mutableStateOf<Band?>(null) }
            val bandId = backStackEntry.arguments?.getString("id")

            LaunchedEffect(bandId, usuarioActual?.id) {
                if(!bandId.isNullOrBlank()) {
                    band = bandFunctions.getBandById(context, bandId, usuarioActual?.id)
                }
            }

            band?.let {
                SingleBandScreen(
                    navController = navController,
                    band = it,
                    user = usuarioActual
                )
            }
        }

        composable("${Routes.ARTISTS.route}/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            ArtistsScreen(
                artists = when(type) {
                    argFavorites -> favArtists
                    argAll -> artists
                    else -> emptyList()
                },
                title = when(type) {
                    argFavorites -> "Mis Artistas"
                    argAll -> "Todos los artistas"
                    else -> "Artistas"
                },
                navController = navController,
                user = usuarioActual
            )
        }

        composable("${Routes.SINGLE_ARTIST.route}/{id}") { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("id")
            var artist by remember { mutableStateOf<Artist?>(null) }

            LaunchedEffect(artistId) {
                if(!artistId.isNullOrBlank()) {
                    artist = bandFunctions.getArtistById(context, artistId, usuarioActual?.id)
                }
            }

            artist?.let {
                SingleArtistScreen(
                    navController = navController,
                    artist = it,
                    user = usuarioActual
                )
            }

        }

        composable("${Routes.SINGLE_ALBUM.route}/{id}") { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("id")

            var album by remember { mutableStateOf<Album?>(null) }

            LaunchedEffect(albumId) {
                if(!albumId.isNullOrBlank()) {
                    album = bandFunctions.getAlbumById(context, albumId, usuarioActual?.id)
                }
            }


            album?.let {
                SingleAlbumScreen(
                    navController = navController,
                    album = it,
                    user = usuarioActual
                )
            }
        }

        composable("${Routes.SINGLE_GENRE.route}/{id}") { backStackEntry ->
            val genreId = backStackEntry.arguments?.getString("id")

            var genre by remember { mutableStateOf<Genre?>(null) }

            LaunchedEffect(genreId) {
                if(!genreId.isNullOrBlank()) {
                    genre = bandFunctions.getGenreById(context, genreId, usuarioActual?.id)
                }
            }

            genre?.let {
                SingleGenreScreen(
                    navController = navController,
                    genre = it,
                    user = usuarioActual
                )
            }
        }
    }
}
