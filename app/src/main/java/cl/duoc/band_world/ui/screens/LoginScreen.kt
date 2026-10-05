package cl.duoc.band_world.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.SessionFunctions
import cl.duoc.band_world.functions.UserFunctions
import cl.duoc.band_world.ui.components.CustomButton
import cl.duoc.band_world.ui.components.CustomInput
import cl.duoc.band_world.ui.components.ErrorAlert
import cl.duoc.band_world.ui.components.LinkText
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType", "ServiceCast")
@Composable
fun LoginScreen(
    navController   : NavController,
    onLoginSuccess  : (User) -> Unit
) {
    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val userFunctions = remember {
        UserFunctions(database.userDao())
    }

    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf<List<String>>(emptyList()) }

    val tone = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp, horizontal = PADDING_HORIZONTAL),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            addHeight()

            CustomInput(
                value           = user,
                label           = "Usuario",
                icon            = Icons.Default.BorderColor,
                onValueChange   = {
                    user = it
                },
            )

            addHeight()

            CustomInput(
                value                   = pass,
                label                   = "Contraseña",
                icon                    = Icons.Default.Lock,
                keyboardType            = KeyboardType.Password,
                visualTransformation    = PasswordVisualTransformation(),
                onValueChange           = {
                    pass = it
                },
            )

            addHeight(17.dp)


            // var usuarioEncontrado by remember { mutableStateOf<User?>(null) }
            CustomButton(
                text    = "Iniciar sesión",
                onClick = {

                    var is_valid = true
                    val list_errors = mutableListOf<String>()
                    errors = emptyList()

                    if(user.isEmpty()) {
                        is_valid = false
                        list_errors.add("Falta Usuario")
                    }

                    if(pass.isEmpty()) {
                        is_valid = false
                        list_errors.add("Falta Contraseña")
                    }

                    scope.launch {
                        val usuarioEncontrado = userFunctions.getUserByUsernamePass(user, pass)

                        if(usuarioEncontrado == null) {
                            is_valid = false
                            list_errors.add("Usuario o contraseña incorrectos")
                        }

                        if(is_valid && usuarioEncontrado != null) {
                            SessionFunctions.saveUserId(context, usuarioEncontrado.id)

                            tone.startTone(ToneGenerator.TONE_PROP_ACK, 300)

                            Toast.makeText(
                                context,
                                "Inicio de sesión correcto",
                                Toast.LENGTH_SHORT
                            ).show()

                            onLoginSuccess(usuarioEncontrado)
                        } else {
                            errors = list_errors
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                            )
                        }
                    }

                }
            )

            addHeight()

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LinkText(
                    title   = "¿Olvidaste tu contraseña?",
                    onClick = {
                        navController.navigate(Routes.RESET.route)
                    }
                )
            }

            addHeight(10.dp)

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LinkText(
                    title   = "Registrarse",
                    onClick = {
                        navController.navigate(Routes.REGISTER.route)
                    }
                )
            }


            if(errors.isNotEmpty()) {
                addHeight(20.dp)
                ErrorAlert(errors)
            }

            addHeight(20.dp)

            HorizontalDivider(thickness = 2.dp)

            addHeight(8.dp)



            Text(
                text = "Usuario de prueba:",
                style = MaterialTheme.typography.bodySmall,
                textDecoration = TextDecoration.Underline
            )
            Text(
                text = "bele_g",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "pass1",
                style = MaterialTheme.typography.bodySmall
            )

        }
    }

}