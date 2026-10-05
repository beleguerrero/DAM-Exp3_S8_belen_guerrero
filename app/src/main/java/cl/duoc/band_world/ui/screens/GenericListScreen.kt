package cl.duoc.band_world.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.band_world.ui.components.CardNoData
import cl.duoc.band_world.ui.components.FavoriteType
import cl.duoc.band_world.ui.components.GeneralCard
import cl.duoc.band_world.ui.components.GeneralCardItem
import cl.duoc.band_world.ui.components.GenreCard
import cl.duoc.band_world.ui.components.GenreCardItem
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.SongCard
import cl.duoc.band_world.ui.components.SongCardItem
import cl.duoc.band_world.ui.components.UserFav
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.components.addWidth
import cl.duoc.band_world.ui.navigation.Routes

@Composable
fun <T> GenericListScreen(
    title       : String? = null,
    composable  : (@Composable () -> Unit)? = null,
    items       : List<T>?,
    circleImage : Boolean = false,
    getDataItem : (T) -> GeneralCardItem,
    userId      : String? = null,
    onClick     : (T) -> Unit,
    favoriteType: FavoriteType,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp, horizontal = PADDING_HORIZONTAL),
        // verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        genericListContent(
            title       = title,
            composable  = composable,
            items       = items,
            circleImage = circleImage,
            userId      = userId,
            onClick     = onClick,
            favoriteType= favoriteType,
            getDataItem = getDataItem
        )

    }
}


fun <T> LazyListScope.genericListContent(
    title               : String? = null,
    composable          : (@Composable () -> Unit)? = null,
    items               : List<T>?,
    circleImage         : Boolean = false,
    getDataItem         : (T) -> GeneralCardItem,
    userId              : String? = null,
    onClick             : (T) -> Unit,
    favoriteType        : FavoriteType,
    chunked             : Int = 2
) {
    if(title != null) {
        item {
            Text(
                text = title
            )
        }
    }

    if (composable != null) {
        item {
            composable.invoke()
        }
    }

    if(!items.isNullOrEmpty()) {
        items(items.chunked(chunked)) { rows ->
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rows.forEach { element ->
                    val getItem = getDataItem(element)

                    val userFav = if(userId != null) {
                        UserFav(
                            userId = userId,
                            favId = getItem.id
                        )
                    } else null

                    when(favoriteType) {
                        FavoriteType.BAND,
                        FavoriteType.ARTIST,
                        FavoriteType.ALBUM-> {
                            GeneralCard(
                                modifier    = Modifier.weight(1f),
                                circleImage = circleImage,
//                                favoriteType= favoriteType,
                                userFav     = userFav,
                                getDataItem = getItem,
                                onClick     = {
                                    onClick(element)
                                }
                            )
                        }

                        FavoriteType.GENRE -> {
                            GenreCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(max = 120.dp),
                                getDataItem = GenreCardItem(
                                    id = getItem.id,
                                    name = getItem.name,
                                    isFavorite = getItem.isFavorite
                                ),
                                userFav = userFav,
                                onClick = {
                                    onClick(element)
                                }
                            )
                        }

                        FavoriteType.SONG -> {
                            SongCard(
                                modifier    = Modifier.padding(horizontal = 5.dp),
                                userFav     = userFav,
                                getDataItem = SongCardItem(
                                    id = getItem.id,
                                    name = getItem.name,
                                    secondLine = getItem.secondLine,
                                    isFavorite = getItem.isFavorite
                                ),
                            )
                        }
                    }
                }
            }
            addHeight(12.dp)
        }
    } else {
        item {
            CardNoData()
        }
    }
}
