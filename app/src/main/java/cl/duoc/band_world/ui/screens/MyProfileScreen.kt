package cl.duoc.band_world.ui.screens

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.User
import cl.duoc.band_world.functions.SessionFunctions
import cl.duoc.band_world.functions.UserFunctions
import cl.duoc.band_world.ui.components.ClassType
import cl.duoc.band_world.ui.components.CustomButton
import cl.duoc.band_world.ui.components.CustomInput
import cl.duoc.band_world.ui.components.ErrorAlert
import cl.duoc.band_world.ui.components.PADDING_HORIZONTAL
import cl.duoc.band_world.ui.components.TituloVerTodo
import cl.duoc.band_world.ui.components.addHeight
import cl.duoc.band_world.utils.Validator
import kotlinx.coroutines.launch
import kotlin.text.isEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    navController   : NavController,
    user            : User?,
    onLogin         : (User) -> Unit,
    onUserUpdated   : (User) -> Unit,
    onLogout        : () -> Unit
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

    var name        by remember { mutableStateOf("") }
    var username    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var pass        by remember { mutableStateOf("") }
    var re_pass     by remember { mutableStateOf("") }
    if(user != null) {
        name        = user.name
        username    = user.username
        email       = user.email
    }

    var errors      by remember { mutableStateOf<List<String>>(emptyList()) }
    var errorsPass  by remember { mutableStateOf<List<String>>(emptyList()) }

    if(user == null) {

        LoginScreen(
            navController   = navController,
            onLoginSuccess  = { loggedUser ->
                onLogin(loggedUser)
            }
        )

    } else {

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
                    text = "¡Hola ${user.username}!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                addHeight()

                TituloVerTodo(
                    titulo  = "Actualizar datos",
                    onClick = null,
                )

                addHeight()

                // NOMBRE
                CustomInput(
                    value           = name,
                    label           = "Nombre",
                    placeholder     = "ej. Edward Cullen",
                    icon            = Icons.Default.BorderColor,
                    onValueChange   = {
                        name = it
                    },
                )

                addHeight()

                // USUARIO
                CustomInput(
                    value           = username,
                    label           = "Usuario",
                    placeholder     = "ej. ed_cullen",
                    icon            = Icons.Default.AlternateEmail,
                    onValueChange   = {
                        username = it
                    },
                )

                addHeight()


                // CORREO
                CustomInput(
                    value           = email,
                    label           = "Correo",
                    placeholder     = "ej. ed@mail.com",
                    icon            = Icons.Default.BorderColor,
                    keyboardType    = KeyboardType.Email,
                    onValueChange   = {
                        email = it
                    },
                )

                addHeight()

                CustomButton(
                    text    = "Actualizar datos",
                    onClick = {

                        var is_valid    = true
                        val list_errors = mutableListOf<String>()
                        errors          = emptyList()
                        errorsPass      = emptyList()

                        if(name.isEmpty()) {
                            is_valid = false
                            list_errors.add("Falta Nombre")
                        }

                        if(username.isEmpty()) {
                            is_valid = false
                            list_errors.add("Falta Usuario")
                        }

                        if(email.isEmpty()) {
                            is_valid = false
                            list_errors.add("Falta Correo")
                        }

                        if(!validator.isEmail(email)) {
                            is_valid = false
                            list_errors.add("Correo inválido")
                        }

                        scope.launch {
                            // username único
                            val usernameActual = user.username
                            if(usernameActual != username) {
                                val usernameEncontrado = userFunctions.getUserByUsername(username)
                                if(usernameEncontrado != null) {
                                    is_valid = false
                                    list_errors.add("Usuario ya utilizado, debe ser único")
                                }
                            }

                            // correo único
                            val emailActual = user.email
                            if(emailActual != email) {
                                val emailEncontrado = userFunctions.getUserByEmail(email)
                                if(emailEncontrado != null) {
                                    is_valid = false
                                    list_errors.add("Correo ya registrado, debe ser único")
                                }
                            }

                            if(is_valid) {
                                val usuarioActualizado = userFunctions.setUser(
                                    userId      = user.id,
                                    name        = name,
                                    username    = username,
                                    email       = email
                                )

                                if(usuarioActualizado != null) {
                                    tone.startTone(ToneGenerator.TONE_PROP_ACK, 300)

                                    Toast.makeText(
                                        context,
                                        "Usuario actualizado",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    onUserUpdated(usuarioActualizado)
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


                if(errors.isNotEmpty()) {
                    addHeight(20.dp)
                    ErrorAlert(errors)
                }

            }

            item {
                addHeight()
                HorizontalDivider(thickness = 2.dp)
                addHeight()
            }

            item {
                TituloVerTodo(
                    titulo = "Cambiar contraseña",
                    onClick = null,
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

                addHeight()

                CustomButton(
                    text    = "Cambiar contraseña",
                    onClick = {
                        var is_valid    = true
                        val list_errors = mutableListOf<String>()
                        errors          = emptyList()
                        errorsPass      = emptyList()

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
                            list_errors.add("Las contraseña deber ser iguales")
                        }

                        scope.launch {

                            if(is_valid) {
                                val usuarioActualizado = userFunctions.setUser(
                                    userId      = user.id,
                                    password    = pass
                                )

                                if(usuarioActualizado != null) {
                                    tone.startTone(ToneGenerator.TONE_PROP_ACK, 300)

                                    Toast.makeText(
                                        context,
                                        "Contraseña actualizada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onUserUpdated(usuarioActualizado)
                                } else {
                                    list_errors.add("Ocurrió un problema")
                                    errorsPass = list_errors
                                    vibrator.vibrate(
                                        VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                                    )
                                }
                            } else {
                                errorsPass = list_errors
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                                )
                            }
                        }
                    }
                )

                if(errorsPass.isNotEmpty()) {
                    addHeight(20.dp)
                    ErrorAlert(errorsPass)
                }
            }

            item {
                addHeight()
                HorizontalDivider(thickness = 2.dp)
                addHeight()
            }

            item {
                CustomButton(
                    text    = "Cerrar sesión",
                    onClick = onLogout,
                    type    = ClassType.DANGER
                )
                addHeight()
            }

        }

    }
}
