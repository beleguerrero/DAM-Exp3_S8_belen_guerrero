package cl.duoc.band_world.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

//    Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

private val BandWorldScheme = darkColorScheme(
    primary =  Color(0xFFd0bcff),
    onPrimary =  Color(0xFF3c0091),
    primaryContainer =  Color(0xFFa078ff),
    onPrimaryContainer =  Color(0xFF340080),
    inversePrimary =  Color(0xFF6d3bd7),

    secondary =  Color(0xFFffb0cd),
    onSecondary =  Color(0xFF640039),
    secondaryContainer =  Color(0xFFaa0266),
    onSecondaryContainer =  Color(0xFFffbad3),

    tertiary =  Color(0xFF4cd7f6),
    onTertiary =  Color(0xFF003640),
    tertiaryContainer =  Color(0xFF009eb9),
    onTertiaryContainer =  Color(0xFF002f38),

    error =  Color(0xFFffb4ab),
    onError =  Color(0xFF690005),
    errorContainer =  Color(0xFF93000a),
    onErrorContainer =  Color(0xFFffdad6),

    surface =  Color(0xFF11131d),
    surfaceDim =  Color(0xFF11131d),
    surfaceBright =  Color(0xFF373844),
    surfaceContainerLowest = Color(0xFF0c0e18),
    surfaceContainerLow =  Color(0xFF191b26),
    surfaceContainer =  Color(0xFF1d1f2a),
    surfaceContainerHigh =  Color(0xFF282934),
    surfaceContainerHighest =  Color(0xFF32343f),
    onSurface =  Color(0xFFe1e1f0),
    onSurfaceVariant =  Color(0xFFcbc3d7),
    inverseSurface =  Color(0xFFe1e1f0),
    inverseOnSurface =  Color(0xFF2e303b),
    surfaceTint =  Color(0xFFd0bcff),
    surfaceVariant =  Color(0xFF32343f),

    background =  Color(0xFF11131d),
    onBackground =  Color(0xFFe1e1f0),

    outline =  Color(0xFF958ea0),
    outlineVariant =  Color(0xFF494454),

    primaryFixed =  Color(0xFFe9ddff),
    primaryFixedDim =  Color(0xFFd0bcff),
    onPrimaryFixed =  Color(0xFF23005c),
    onPrimaryFixedVariant =  Color(0xFF5516be),

    secondaryFixed =  Color(0xFFffd9e4),
    secondaryFixedDim =  Color(0xFFffb0cd),
    onSecondaryFixed =  Color(0xFF3e0022),
    onSecondaryFixedVariant =  Color(0xFF8c0053),

    tertiaryFixed =  Color(0xFFacedff),
    tertiaryFixedDim =  Color(0xFF4cd7f6),
    onTertiaryFixed =  Color(0xFF001f26),
    onTertiaryFixedVariant =  Color(0xFF004e5c),
)

@Composable
fun BandWorldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = BandWorldScheme,
        typography = Typography,
        content = content
    )
}