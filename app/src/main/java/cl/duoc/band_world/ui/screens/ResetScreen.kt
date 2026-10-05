package cl.duoc.band_world.ui.screens

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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.functions.BandFunctions
import cl.duoc.band_world.functions.FavoriteFunctions
import cl.duoc.band_world.functions.UserFunctions
import cl.duoc.band_world.ui.components.CustomButton
import cl.duoc.band_world.ui.components.CustomInput
import cl.duoc.band_world.ui.components.ErrorAlert
import cl.duoc.band_world.ui.components.LinkText
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.ui.navigation.Routes
import cl.duoc.band_world.utils.Validator
import kotlinx.coroutines.launch

@Composable
fun ResetScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val database = remember {
        BandWorldDatabase.getDatabase(context)
    }
    val userFunctions = remember {
        UserFunctions(database.userDao())
    }

    val scope = rememberCoroutineScope()

    val validator = Validator()

    var email   by remember { mutableStateOf("") }
    var pass    by remember { mutableStateOf("") }
    var re_pass by remember { mutableStateOf("") }

    var errors  by remember { mutableStateOf<List<String>>(emptyList()) }

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
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            addHeight()


            // CORREO
            CustomInput(
                value           = email,
                label           = "Correo",
                placeholder     = "ej. ed@mail.com",
                icon            = Icons.Default.AlternateEmail,
                keyboardType    = KeyboardType.Email,
                onValueChange   = {
                    email = it
                },
            )

            addHeight()

            // CONTRASEÑA
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

            addHeight()


            // REPITA CONTRASEÑA
            CustomInput(
                value                   = re_pass,
                label                   = "Repita contraseña",
                icon                    = Icons.Default.Lock,
                keyboardType            = KeyboardType.Password,
                visualTransformation    = PasswordVisualTransformation(),
                onValueChange           = {
                    re_pass = it
                },
            )

            addHeight(17.dp)

            CustomButton(
                text    = "Cambiar contraseña",
                onClick = {
                    var is_valid    = true
                    val list_errors = mutableListOf<String>()
                    errors          = emptyList()


                    if(email.isEmpty()) {
                        is_valid = false
                        list_errors.add("Falta Correo")
                    }
                    if(!validator.isEmail(email)) {
                        is_valid = false
                        list_errors.add("Correo inválido")
                    }

                    if(pass.isEmpty()) {
                        is_valid = false
                        list_errors.add("Falta Contraseña")
                    }
                    if(!validator.isPassValid(pass)) {
                        is_valid = false
                        list_errors.add("La contraseña debe contener letras y números")
                    }

                    if(re_pass.isEmpty()) {
                        is_valid = false
                        list_errors.add("Falta Repita contraseña")
                    }

                    if(pass != re_pass) {
                        is_valid = false
                        list_errors.add("Las contraseñas deben ser iguales")

                    }

                    scope.launch {
                        val userEncontrado = userFunctions.getUserByEmail(email)
                        if(userEncontrado == null) {
                            is_valid = false
                            list_errors.add("Correo no encontrado")
                        }

                        // reset correcto!
                        if(is_valid && userEncontrado != null) {
                            val usuarioActualizado = userFunctions.setUser(
                                userId = userEncontrado.id,
                                password = pass
                            )

                            if(usuarioActualizado != null) {
                                tone.startTone(ToneGenerator.TONE_PROP_ACK, 300)

                                Toast.makeText(
                                    context,
                                    "Recuperación de contraseña correcto",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.navigate(Routes.LOGIN.route) {
                                    popUpTo(Routes.RESET.route) {
                                        inclusive = true
                                    }
                                }

                            } else {
                                list_errors.add("Ocurrió un problema")
                                errors = list_errors
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                                )
                            }
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
                    title = "Iniciar sesión",
                    onClick = {
                        navController.navigate(Routes.LOGIN.route)
                    }
                )
            }


            if(errors.isNotEmpty()) {
                addHeight(20.dp)
                ErrorAlert(errors)
            }
        }
    }
}
