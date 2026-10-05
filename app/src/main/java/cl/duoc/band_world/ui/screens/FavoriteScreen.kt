package cl.duoc.band_world.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.Album
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.SongBandName
import cl.duoc.band_world.ui.components.CardNoData
import cl.duoc.band_world.ui.components.CustomInput
import cl.duoc.band_world.ui.components.CustomScrollableTab
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.FilterButton
import cl.duoc.band_world.ui.components.FilterType
import cl.duoc.band_world.ui.components.GeneralCard
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.GenreCard
import cl.duoc.band_world.ui.components.GenreCardItem
import cl.duoc.band_world.ui.components.ListaFiltro
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.SingleInfoAlert
import cl.duoc.band_world.ui.components.SongCard
import cl.duoc.band_world.ui.components.SongCardItem
import cl.duoc.band_world.ui.components.TituloVerTodo
import cl.duoc.band_world.ui.components.UserFav
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes


@Composable
fun FavoriteScreen(
    navController   : NavController,
    user            : User?,
) {
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


    var selectedTab by remember { mutableIntStateOf(0) }

    val bandsScrollState    = rememberScrollState()
    val artistsScrollState  = rememberScrollState()
    val albumsScrollState   = rememberScrollState()
    val songsScrollState    = rememberScrollState()
    val genresScrollState   = rememberScrollState()


    var favBands        by remember { mutableStateOf<List<Band>>(emptyList()) }
    var favBandsCount   by remember { mutableStateOf(0) }

    var favArtists      by remember { mutableStateOf<List<Artist>>(emptyList()) }
    var favArtistsCount by remember { mutableStateOf(0) }

    var favAlbums       by remember { mutableStateOf<List<Album>>(emptyList()) }
    var favAlbumsCount  by remember { mutableStateOf(0) }

    var favSongs        by remember { mutableStateOf<List<SongBandName>>(emptyList()) }
    var favSongsCount   by remember { mutableStateOf(0) }

    var favGenres       by remember { mutableStateOf<List<Genre>>(emptyList()) }
    var favGenresCount  by remember { mutableStateOf(0) }

    var loading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        try {
            favBands = if(user != null) {
                favoriteFunctions.getUserFavoriteBands(context, user.id)
            } else emptyList()
            favBandsCount = favBands?.size ?: 0


            favArtists = if(user != null) {
                favoriteFunctions.getUserFavoriteArtists(context, user.id)
            } else emptyList()
            favArtistsCount = favArtists?.size ?: 0


            favAlbums = if(user != null) {
                favoriteFunctions.getUserFavoriteAlbums(context, user.id)
            } else emptyList()
            favAlbumsCount = favAlbums?.size ?: 0


            favSongs = if(user != null) {
                favoriteFunctions.getUserFavoriteSongs(context, user.id)
            } else emptyList()
            favSongsCount = favSongs?.size ?: 0


            favGenres = if(user != null) {
                favoriteFunctions.getUserFavoriteGenres(user.id)
            } else emptyList()
            favGenresCount = favGenres?.size ?: 0

        } catch (e: Exception) {
            println("*** ERROR: ${e.message}")
        }
        loading = false
    }

    if(loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }


    val tabs = listOf(
        "Todos (${favBandsCount + favArtistsCount + favAlbumsCount + favSongsCount + favGenresCount})",
        "Bandas (${favBandsCount})",
        "Artistas (${favArtistsCount})",
        "Álbumes (${favAlbumsCount})",
        "Canciones (${favSongsCount})",
        "Géneros (${favGenresCount})"
    )


    var searchTextBand      by remember { mutableStateOf("") }
    var searchTextArtist    by remember { mutableStateOf("") }
    var searchTextAlbum     by remember { mutableStateOf("") }
    var searchTextSong      by remember { mutableStateOf("") }
    var searchTextGenre     by remember { mutableStateOf("") }

    var filterIncludesBand by remember { mutableStateOf<List<ListaFiltro>>(emptyList()) }
    var filterExcludesBand by remember { mutableStateOf<List<ListaFiltro>>(emptyList()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp, horizontal = PADDING_HORIZONTAL),
    ) {

        item {
            Text(
                text = "Mis Favoritos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            addHeight()
        }

        if(user == null) {
            item {
                SingleInfoAlert("Debe iniciar sesión")
                addHeight(20.dp)
            }
        }

        item {
            CustomScrollableTab(
                selectedTab = selectedTab,
                listTabs    = tabs,
                onSelected  = { index ->
                    selectedTab = index
                }
            )
        }



        val tabTodos        = 0
        val tabBandas       = 1
        val tabArtistas     = 2
        val tabAlbumes      = 3
        val tabCanciones    = 4
        val tabGeneros      = 5

        when (selectedTab) {
            // TODOS
            tabTodos -> {
                item {
                    addHeight()
                    TituloVerTodo(
                        titulo  = "Bandas",  // últimas bandas??
                        link    = "mis bandas favoritas",
                        onClick = { selectedTab = tabBandas },
                    )
                    addHeight(5.dp)
                    if (favBands.isNotEmpty()) {
                        ItemsHorizontalCard(
                            navController       = navController,
                            horizontalScroll    = bandsScrollState,
                            items               = favBands.take(10),
                            favoriteType        = FavoriteType.BAND,
                            userId              = user?.id,
                            getDataItem         = {
                                GeneralCardItem(
                                    id          = it.id,
                                    name        = it.name,
                                    image       = it.image,
                                    isFavorite  = it.isFavorite
                                )
                            },
                        )
                    } else {
                        CardNoData()
                    }
                }

                item {
                    addHeight(30.dp)
                    TituloVerTodo(
                        titulo  = "Artistas",
                        link    = "mis artistas favoritos",
                        onClick = { selectedTab = tabArtistas },
                    )
                    addHeight(5.dp)
                    if (favArtists.isNotEmpty()) {
                        ItemsHorizontalCard(
                            navController       = navController,
                            horizontalScroll    = artistsScrollState,
                            items               = favArtists.take(10),
                            circleImage         = true,
                            favoriteType        = FavoriteType.ARTIST,
                            userId              = user?.id,
                            getDataItem         = {
                                GeneralCardItem(
                                    id          = it.id,
                                    name        = it.name,
                                    image       = it.image,
                                    isFavorite  = it.isFavorite
                                )
                            },
                        )
                    } else {
                        CardNoData()
                    }
                }

                item {
                    addHeight(30.dp)
                    TituloVerTodo(
                        titulo  = "Géneros",
                        link    = "mis géneros favoritos",
                        onClick = { selectedTab = tabGeneros },
                    )
                    addHeight(5.dp)
                    if (favGenres.isNotEmpty()) {
                        ItemsHorizontalCard(
                            navController       = navController,
                            horizontalScroll    = genresScrollState,
                            items               = favGenres.take(10),
                            circleImage         = true,
                            favoriteType        = FavoriteType.GENRE,
                            userId              = user?.id,
                            getDataItem         = {
                                GeneralCardItem(
                                    id          = it.id,
                                    name        = it.name,
                                    image       = null,
                                    isFavorite  = it.isFavorite
                                )
                            },
                        )
                    } else {
                        CardNoData()
                    }
                }

                item {
                    addHeight()
                    TituloVerTodo(
                        titulo  = "Álbumes",
                        link    = "mis álbumes favoritos",
                        onClick = { selectedTab = tabAlbumes },
                    )
                    addHeight(5.dp)
                    if (favAlbums.isNotEmpty()) {
                        ItemsHorizontalCard(
                            navController       = navController,
                            horizontalScroll    = albumsScrollState,
                            items               = favAlbums,
                            favoriteType        = FavoriteType.ALBUM,
                            userId              = user?.id,
                            getDataItem         = {
                                GeneralCardItem(
                                    id          = it.id,
                                    name        = it.name,
                                    image       = it.image,
                                    isFavorite  = it.isFavorite
                                )
                            },
                        )
                    } else {
                        CardNoData()
                    }
                }

                item {
                    addHeight()
                    TituloVerTodo(
                        titulo  = "Canciones",
                        link    = "mis canciones favoritas",
                        onClick = { selectedTab = tabCanciones },
                    )
                    addHeight(5.dp)
                    if (favSongs.isNotEmpty()) {
                        ItemsHorizontalCard(
                            navController       = navController,
                            horizontalScroll    = songsScrollState,
                            items               = favSongs,
                            circleImage         = true,
                            favoriteType        = FavoriteType.SONG,
                            userId              = user?.id,
                            getDataItem         = {
                                GeneralCardItem(
                                    id          = it.song.id,
                                    name        = it.song.name,
                                    secondLine  = it.bandName.toString(),
                                    image       = null,
                                    isFavorite  = it.song.isFavorite
                                )
                            },
                        )
                    } else {
                        CardNoData()
                    }
                }
            }





            // BANDAS
            tabBandas -> {
                item {
                    addHeight()
                    TituloVerTodo(
                        titulo  = "Bandas",
                        onClick = { navController.navigate("${Routes.BANDS.route}/all") },
                    )
                    addHeight(5.dp)
                }

                val favBands_ = bandFunctions
                    .getBandsFilter(
                        bands       = favBands,
                        includes    = filterIncludesBand,
                        excludes    = filterExcludesBand
                    )
                    ?.filter { it.name.contains(searchTextBand, ignoreCase = true) }
                genericListContent(
                    items       = favBands_,
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
                    onClick     = { item ->
                        navController.navigate("${Routes.SINGLE_BAND.route}/${item.id}")
                    },
                    composable  = {
                        if(favBands.isNotEmpty()) {
                            CustomInput(
                                value           = searchTextBand,
                                placeholder     = "Ingrese para buscar...",
                                icon            = Icons.Default.Search,
                                onValueChange   = {
                                    searchTextBand = it
                                },
                                endButton       = {
                                    FilterButton(
                                        bandFunctions   = bandFunctions,
                                        filterOption    = FilterType.BANDS,
                                        includes        = filterIncludesBand,
                                        excludes        = filterExcludesBand,
                                        onChange        = { newIncludes, newExcludes ->
                                            filterIncludesBand = newIncludes
                                            filterExcludesBand = newExcludes
                                        }
                                    )
                                }
                            )
                            addHeight()
                        }
                    }
                )

            }




            // ARTISTAS
            tabArtistas -> {
                item {
                    addHeight()
                    TituloVerTodo(
                        titulo      = "Artistas",
                         onClick    = { navController.navigate("${Routes.ARTISTS.route}/all") },
                    )
                    addHeight(5.dp)
                }

                val favArtists_ = favArtists?.filter { it.name.contains(searchTextArtist, ignoreCase = true) }
                genericListContent(
                    items       = favArtists_,
                    getDataItem = {
                        GeneralCardItem(
                            id = it.id,
                            name = it.name,
                            image = it.image,
                            isFavorite = it.isFavorite
                        )
                    },
                    userId      = user?.id,
                    circleImage = true,
                    favoriteType= FavoriteType.ARTIST,
                    onClick     = { item ->
                        navController.navigate("${Routes.SINGLE_ARTIST.route}/${item.id}")
                    },
                    composable  = {
                        if(favArtists.isNotEmpty()) {
                            CustomInput(
                                value           = searchTextArtist,
                                placeholder     = "Ingrese para buscar...",
                                icon            = Icons.Default.Search,
                                onValueChange   = {
                                    searchTextArtist = it
                                },
                            )
                            addHeight()
                        }
                    }
                )


            }




            // ÁLBUMES
            tabAlbumes -> {
                item {
                    addHeight()
                    TituloVerTodo(
                        titulo = "Álbumes",
                    )
                    addHeight(5.dp)
                }

                val favAlbums_ = favAlbums?.filter { it.name.contains(searchTextAlbum, ignoreCase = true) }
                genericListContent(
                    items       = favAlbums_,
                    getDataItem = {
                        GeneralCardItem(
                            id = it.id,
                            name = it.name,
                            image = it.image,
                            isFavorite = it.isFavorite
                        )
                    },
                    userId      = user?.id,
                    favoriteType= FavoriteType.ALBUM,
                    onClick     = { item ->
                        navController.navigate("${Routes.SINGLE_ALBUM.route}/${item.id}")
                    },
                    composable  = {
                        if(favAlbums.isNotEmpty()) {
                            CustomInput(
                                value           = searchTextAlbum,
                                placeholder     = "Ingrese para buscar...",
                                icon            = Icons.Default.Search,
                                onValueChange   = {
                                    searchTextAlbum = it
                                },
                            )
                            addHeight()
                        }
                    }
                )
            }





            // CANCIONES
            tabCanciones -> {
                item {
                    addHeight()
                    TituloVerTodo(
                        titulo = "Canciones",
                    )
                    addHeight(5.dp)
                }

                val favSongs_ = favSongs?.filter { it.song.name.contains(searchTextSong, ignoreCase = true) }
                genericListContent(
                    items               = favSongs_,
                    getDataItem         = {
                        GeneralCardItem(
                            id = it.song.id,
                            name = it.song.name,
                            image = null,
                            secondLine = it.bandName.toString(),
                            isFavorite = it.song.isFavorite
                        )
                    },
                    userId              = user?.id,
                    favoriteType        = FavoriteType.SONG,
                    onClick             = { },
                    chunked             = 1,
                    composable          = {
                        if(favSongs.isNotEmpty()) {
                            CustomInput(
                                value           = searchTextSong,
                                placeholder     = "Ingrese para buscar...",
                                icon            = Icons.Default.Search,
                                onValueChange   = {
                                    searchTextSong = it
                                },
                            )
                            addHeight()
                        }
                    }
                )
            }




            // GÉNEROS
            tabGeneros -> {
                item {
                    addHeight()
                    TituloVerTodo(
                        titulo      = "Géneros",
                         onClick    = { navController.navigate("${Routes.GENRES.route}/all") },
                    )
                    addHeight(5.dp)
                }

                val favGenre_ = favGenres?.filter { it.name.contains(searchTextGenre, ignoreCase = true) }
                genericListContent(
                    items       = favGenre_,
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
                    onClick     = { item ->
                        navController.navigate("${Routes.SINGLE_GENRE.route}/${item.id}")
                    },
                    composable  = {
                        if(favGenres.isNotEmpty()) {
                            CustomInput(
                                value           = searchTextGenre,
                                placeholder     = "Ingrese para buscar...",
                                icon            = Icons.Default.Search,
                                onValueChange   = {
                                    searchTextGenre = it
                                },
                            )
                            addHeight()
                        }
                    }
                )
            }
        }

    }
}



@Composable
fun <T> ItemsHorizontalCard(
    navController       : NavController,
    horizontalScroll    : ScrollState? = null,
    items               : List<T>,
    circleImage         : Boolean = false,
    getDataItem         : (T) -> GeneralCardItem,
    userId              : String? = null,
    favoriteType        : FavoriteType
) {
    val modifierRow = if(horizontalScroll != null) {
        Modifier
            .fillMaxWidth()
            .horizontalScroll(horizontalScroll)
    } else {
        Modifier.fillMaxWidth()
    }
    Row(
        modifier = modifierRow,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items.forEach { item ->
            val getItem = getDataItem(item)
            val userFav = if(userId != null) {
                UserFav(
                    userId  = userId,
                    favId   = getItem.id
                )
            } else null

            when(favoriteType) {
                FavoriteType.BAND,
                FavoriteType.ARTIST,
                FavoriteType.ALBUM -> {
                    GeneralCard (
                        modifier    = Modifier.width(150.dp),
                        circleImage = circleImage,
                        getDataItem = getItem,
                        userFav     = userFav,
                        onClick     = {
                            if(favoriteType == FavoriteType.BAND) navController.navigate("${Routes.SINGLE_BAND.route}/${getItem.id}")
                            if(favoriteType == FavoriteType.ARTIST) navController.navigate("${Routes.SINGLE_ARTIST.route}/${getItem.id}")
                            if(favoriteType == FavoriteType.ALBUM) navController.navigate("${Routes.SINGLE_ALBUM.route}/${getItem.id}")
                        },
                    )
                }

                FavoriteType.GENRE -> {
                    GenreCard(
                        modifier = Modifier
                            .widthIn(max = 190.dp)
                            .heightIn(max = 110.dp),
                        getDataItem = GenreCardItem(
                            id = getItem.id,
                            name = getItem.name,
                            isFavorite = getItem.isFavorite,
                        ),
                        userFav = userFav,
                        onClick = {
                            navController.navigate("${Routes.SINGLE_GENRE.route}/${getItem.id}")
                        },
                    )
                }

                FavoriteType.SONG -> {
                    SongCard(
                        modifier = Modifier
                            .widthIn(max = 230.dp)
                            .fillMaxWidth(),
                        userFav     = userFav,
                        getDataItem = SongCardItem(
                            id = getItem.id,
                            name = getItem.name,
                            secondLine = getItem.secondLine,
                            isFavorite = getItem.isFavorite,
                        ),
                    )
                }
            }
        }
    }
}

