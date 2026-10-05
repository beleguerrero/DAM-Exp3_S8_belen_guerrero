package cl.duoc.band_world.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

private enum class AlertType { ERROR, INFO }

private data class AlertColor(
    val background: Color,
    val text: Color,
    val icon: Color
)

@Composable
private fun getColors(type: AlertType): AlertColor {
    return when(type) {
        AlertType.ERROR -> AlertColor(
            background = MaterialTheme.colorScheme.errorContainer,
            text = MaterialTheme.colorScheme.onErrorContainer,
            icon = MaterialTheme.colorScheme.error
        )

        AlertType.INFO -> AlertColor(
            background = MaterialTheme.colorScheme.primaryContainer,
            text = MaterialTheme.colorScheme.onPrimaryContainer,
            icon = MaterialTheme.colorScheme.primary,
        )
    }
}

private data class AlertIcon(
    val iconVector: ImageVector
)

@Composable
private fun getIcon(type: AlertType): AlertIcon {
    return when(type) {
        AlertType.ERROR -> AlertIcon(Icons.Default.Error)
        AlertType.INFO -> AlertIcon(Icons.Default.Info)
    }
}



@Composable
private fun BaseAlert(
    listMessage: List<String>? = null,
    singleLine: String? = null,
    alertType: AlertType,
) {
    val colors = getColors(type = alertType)
    val imageVector = getIcon(type = alertType)

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.background,
                shape = RoundedCornerShape(3.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector.iconVector,
                contentDescription = "",
                tint = colors.icon
            )
            addWidth(8.dp)
            if(listMessage != null) {
                Text(
                    text = "¡Atención!",
                    color = colors.text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline
                )
            } else {
                if(singleLine != null) {
                    Text(
                        text = singleLine,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        if(listMessage != null) {
            addHeight(5.dp)

            listMessage.forEach { m ->
                Text(
                    text = "- ${m}",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}



@Composable
fun ErrorAlert(
    lista_errores: List<String>
) {
    BaseAlert(
        listMessage = lista_errores,
        alertType = AlertType.ERROR
    )
}

@Composable
fun SingleInfoAlert(
    message: String
) {
    BaseAlert(
        singleLine = message,
        alertType = AlertType.INFO
    )
}

@Composable
fun InfoAlert(
    lista_info: List<String>
) {
    BaseAlert(
        listMessage = lista_info,
        alertType = AlertType.INFO
    )
}



