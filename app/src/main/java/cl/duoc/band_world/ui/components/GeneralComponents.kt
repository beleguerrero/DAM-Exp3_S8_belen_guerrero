package cl.duoc.band_world.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cl.duoc.band_world.R
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.UserFunctions
import kotlinx.coroutines.launch


val PADDING_HORIZONTAL = 20.dp

@Composable
fun TituloVerTodo(
    titulo: String,
    link: String = "VER TODOS",
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        if(onClick != null) {
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .clickable { onClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = link.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                addWidth(4.dp)

                Icon(
                    imageVector = Icons.Default.ArrowRightAlt,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )

            }
        }
    }
}


enum class TypeButtonFav { SEGUIR, SIGUIENDO }
enum class FavoriteType { BAND, ARTIST, ALBUM, GENRE, SONG }
@Composable
fun BaseBotonFav(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    type: TypeButtonFav,
    onClick: () -> Unit
) {
    val title = when(type) {
        TypeButtonFav.SEGUIR -> "Seguir"
        TypeButtonFav.SIGUIENDO -> "Siguiendo"
    }
    val icon = when(type) {
        TypeButtonFav.SEGUIR -> Icons.Default.FavoriteBorder
        TypeButtonFav.SIGUIENDO -> Icons.Default.Favorite
    }
    if(compact) {

        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface.copy(alpha = .8f))
                .size(30.dp)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.secondary
            )
        }

    } else {

        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            onClick = onClick,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title
            )

            addWidth(8.dp)

            Text(title)

        }
    }
}

@Composable
fun ImageSingleContent(
    image: Int?,
    hideImage: Boolean = false,
    title: String,
    extraText: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (!hideImage) {
                    Modifier.height(250.dp)
                } else {
                    Modifier
                }
            )
    ) {
        if(!hideImage) {
            Image(
                painter = painterResource(
                    id = image ?: R.drawable.sin_imagen
                ),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = PADDING_HORIZONTAL),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = extraText,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall

            )

            addHeight(5.dp)
        }
    }
}

@Composable
fun DescriptionCard(
    modifier: Modifier = Modifier,
    title: String = "Acerca de",
    description: String,
    expanded: Boolean,
    onExpandedChange: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                maxLines = if(expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier
                    .clickable { onExpandedChange() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if(expanded) "Menos" else "Leer más",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                addWidth(4.dp)

                Icon(
                    imageVector = if(expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Ver todo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )

            }
        }
    }
}



data class UserFav(
    val userId: String,
    val favId: String,
)

data class GeneralCardItem(
    val id: String,
    val name: String,
    val image: Int?,
    val secondLine: String = "",
    val isFavorite: Boolean
)
@Composable
fun GeneralCard(
    modifier: Modifier = Modifier,
    heightImage: Dp = 150.dp,
    circleImage: Boolean = false,
    thirdLine: (@Composable () -> Unit)? = null,
    getDataItem: GeneralCardItem,
    userFav: UserFav? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(circleImage) {
                Color.Transparent
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if(circleImage) {
                Color.Transparent
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            var imageModifier = Modifier
                .fillMaxWidth()
                .height(heightImage)
            if(circleImage) {
                imageModifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
            }

            Image(
                painter = painterResource(
                    id = getDataItem.image ?: R.drawable.sin_imagen
                ),
                contentDescription = getDataItem.name,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )

            if(userFav != null) {
                if(getDataItem.isFavorite) {

                    BaseBotonFav(
                        modifier = Modifier.align(Alignment.TopEnd).padding(5.dp).clip(CircleShape),
                        compact = true,
                        type = TypeButtonFav.SIGUIENDO,
                        onClick = {}
                    )
                }
            }


        }

        addHeight(5.dp)

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = getDataItem.name,
                style = MaterialTheme.typography.titleMedium
            )

            if(!getDataItem.secondLine.isNullOrBlank()) {
                Text(
                    text = getDataItem.secondLine,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            thirdLine?.invoke()

            addHeight(5.dp)

        }
    }
}


data class GenreCardItem(
    val id: String,
    val name: String,
    val secondLine: String? = null,
    val isFavorite: Boolean
)
@Composable
fun GenreCard(
    modifier    : Modifier = Modifier,
    getDataItem : GenreCardItem,
    userFav     : UserFav? = null,
    onClick     : () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerLowest,
            MaterialTheme.colorScheme.surfaceContainerHigh
        )
    )

    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient),
        ) {

            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                addHeight(10.dp)
                Text(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    text = getDataItem.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                if(!getDataItem.secondLine.isNullOrBlank()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 15.dp),
                        text = getDataItem.secondLine,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                addHeight(45.dp)

            }

            if(userFav != null) {
                val typeButtonFav = when(getDataItem.isFavorite) {
                    true -> TypeButtonFav.SIGUIENDO
                    false -> TypeButtonFav.SEGUIR
                }
                BaseBotonFav(
                    modifier = Modifier.align(Alignment.TopEnd).padding(5.dp).clip(CircleShape),
                    compact = true,
                    type = typeButtonFav,
                    onClick = {}
                )
            }
        }


    }
}


data class SongCardItem(
    val id: String,
    val name: String,
    val number: String? = null,
    val secondLine: String? = null,
    val isFavorite: Boolean
)
@Composable
fun SongCard(
    modifier: Modifier = Modifier,
    getDataItem: SongCardItem,
    userFav: UserFav? = null,
) {
    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val favoriteFunctions = remember {
        FavoriteFunctions(database.bandDao(),database.userDao())
    }

    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "Canción",
            )
            addWidth(10.dp)

            if(!getDataItem.number.isNullOrBlank()) {
                Text(
                    text = "%02d".format(getDataItem.number.toInt()),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                 addWidth(8.dp)
            }

            Column(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = getDataItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if(!getDataItem.secondLine.isNullOrBlank()) {
                    Text(
                        text = getDataItem.secondLine,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            if(userFav != null) {
                Spacer(modifier = Modifier.weight(1f))
                var isFavorite by remember {
                    mutableStateOf(getDataItem.isFavorite)
                }
                val typeButtonFav = when(isFavorite) {
                    true -> TypeButtonFav.SIGUIENDO
                    false -> TypeButtonFav.SEGUIR
                }
                BaseBotonFav(
                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    compact = true,
                    type = typeButtonFav,
                    onClick = {
                        scope.launch {
                            favoriteFunctions.toggleFavorite(
                                userId = userFav.userId,
                                itemId = getDataItem.id,
                                isFavorite = isFavorite,
                                type = FavoriteType.SONG
                            )
                            isFavorite = !isFavorite
                        }
                    }
                )
            }
        }
    }

}



@Composable
fun CustomInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String = "Ingrese ${label}",
    icon: ImageVector,
    endButton: (@Composable () -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if(!label.isNullOrBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            addHeight(5.dp)
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            visualTransformation = visualTransformation,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(40.dp)
                )
                .border(
                    width = 1.dp,
                    color = backgroundColor,
                    shape = RoundedCornerShape(40.dp)
                ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(
                        horizontal = 15.dp,
                        vertical = if(endButton == null) {
                            12.dp
                        } else {
                            0.dp
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    addWidth(6.dp)
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(18.dp)
                    )
                    addWidth(8.dp)

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if(value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        innerTextField()
                    }

                    endButton?.invoke()
                }
            }
        )
    }
}


enum class ClassType { PRIMARY, SECONDARY, TERTIARY, DANGER }
private data class ButtonColor(
    val containerColor: Color,
    val contentColor: Color,
    val ambientColor: Color,
    val spotColor: Color,
)

@Composable
private fun getButtonColors(type: ClassType): ButtonColor {
    return when (type) {
        ClassType.PRIMARY -> ButtonColor(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            ambientColor = MaterialTheme.colorScheme.primary,
            spotColor = MaterialTheme.colorScheme.primary
        )
        ClassType.SECONDARY -> ButtonColor(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            ambientColor = MaterialTheme.colorScheme.secondary,
            spotColor = MaterialTheme.colorScheme.secondary
        )
        ClassType.TERTIARY -> ButtonColor(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            ambientColor = MaterialTheme.colorScheme.tertiary,
            spotColor = MaterialTheme.colorScheme.tertiary
        )
        ClassType.DANGER -> ButtonColor(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            ambientColor = MaterialTheme.colorScheme.error,
            spotColor = MaterialTheme.colorScheme.error
        )
    }
}


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    type: ClassType = ClassType.PRIMARY
) {
    val colors = getButtonColors(type)

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(40.dp),
                ambientColor = colors.ambientColor,
                spotColor = colors.spotColor
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
fun CustomScrollableTab(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    listTabs: List<String>,
    onSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTab,
        edgePadding = 0.dp,
        indicator = {},
        divider = {},
    ) {

        listTabs.forEachIndexed { index, key ->
            Surface(
                modifier = Modifier
                    .clickable {
                        onSelected(index)
                    }
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(50),
                color = if(selectedTab == index) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                }
            ) {
                Text(
                    text = key,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    color = if(selectedTab == index) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}


@Composable
fun LinkText(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        textDecoration = TextDecoration.Underline,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .clickable { onClick() }
    )
}

@Composable
fun CardNoData(
    modifier: Modifier = Modifier,
    title: String = "No hay datos :("
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 5.dp)
        ) {

            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun addHeight(height: Dp = 15.dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun addWidth(width: Dp = 15.dp) {
    Spacer(modifier = Modifier.width(width))
}

