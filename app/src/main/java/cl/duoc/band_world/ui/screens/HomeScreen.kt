package cl.duoc.band_world.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.GeneralCard
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.GenreCard
import cl.duoc.band_world.ui.components.GenreCardItem
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.TituloVerTodo
import cl.duoc.band_world.ui.components.UserFav
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController   : NavController,
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
        FavoriteFunctions(database.bandDao(), database.userDao())
    }

    val username = user?.username ?: ""
    val topBandsScrollState     = rememberScrollState()
    val topArtistsScrollState   = rememberScrollState()

    var topBands    by remember { mutableStateOf<List<Band>>(emptyList()) }
    var topArtists  by remember { mutableStateOf<List<Artist>>(emptyList()) }
    var topGenres   by remember { mutableStateOf<List<Genre>>(emptyList()) }
    var countBands  by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }


    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(user?.id) {
        try {
            topBands    = favoriteFunctions.getTopBands(context, user?.id)
            topArtists  = favoriteFunctions.getTopArtists(context, user?.id)
            topGenres   = favoriteFunctions.getTopGenres(user?.id, limit = 6)
            countBands  = bandFunctions.getCountBandsByGenre()

        } catch (e: Exception) {
            println("*** ERROR: ${e.message}")
            e.printStackTrace()
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


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp, horizontal = PADDING_HORIZONTAL),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "¡Hola ${username}!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            addHeight()
            TituloVerTodo(
                titulo  = "Top Bandas",
                onClick = { navController.navigate("${Routes.BANDS.route}/all") },
            )
            addHeight(5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(topBandsScrollState),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                topBands.forEach { band ->
                    val userFav = if(user != null) {
                        UserFav(
                            userId  = user.id,
                            favId   = band.id
                        )
                    } else null

                    GeneralCard (
                        modifier    = Modifier.width(150.dp),
                        userFav = userFav,
                        getDataItem = GeneralCardItem(
                            id = band.id,
                            name = band.name,
                            image = band.image,
                            isFavorite = band.isFavorite
                        ),
//                        favoriteType= FavoriteType.BAND,
                        onClick     = {
                            navController.navigate("${Routes.SINGLE_BAND.route}/${band.id}")
                        }
                    )
                }
            }
        }

        item {
            addHeight(30.dp)
            TituloVerTodo(
                titulo  = "Top Artistas",
                onClick = { navController.navigate("${Routes.ARTISTS.route}/all") },
            )
            addHeight(5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(topArtistsScrollState),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                topArtists.forEach { artist ->
                    val userFav = if(user != null) {
                        UserFav(
                            userId  = user.id,
                            favId   = artist.id
                        )
                    } else null

                    GeneralCard (
                        modifier    = Modifier.width(150.dp),
                        userFav = userFav,
                        circleImage = true,
                        getDataItem = GeneralCardItem(
                            id = artist.id,
                            name = artist.name,
                            image = artist.image,
                            isFavorite = artist.isFavorite
                        ),
                        onClick     = {
                            navController.navigate("${Routes.SINGLE_ARTIST.route}/${artist.id}")
                        }
                    )
                }
            }
        }


        item {
            addHeight(30.dp)
            TituloVerTodo(
                titulo = "Géneros",
            )
        }

        items(topGenres.chunked(2)) { rowGenre ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowGenre.forEach { genre ->
                    val userFav = if(user != null) {
                        UserFav(
                            userId  = user.id,
                            favId   = genre.id
                        )
                    } else null

                    GenreCard(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(max = 120.dp),
                        userFav = userFav,
                        getDataItem = GenreCardItem(
                            id = genre.id,
                            name = genre.name,
                            secondLine = "${countBands[genre.id]} Bandas",
                            isFavorite = genre.isFavorite
                        ),
                        onClick     = {
                            navController.navigate("${Routes.SINGLE_GENRE.route}/${genre.id}")
                        }
                    )
                }
            }
        }
    }
}
