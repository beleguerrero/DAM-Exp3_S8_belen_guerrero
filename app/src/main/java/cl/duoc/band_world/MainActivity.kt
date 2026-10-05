package cl.duoc.band_world

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import cl.duoc.band_world.data.BandWorldDatabase
import cl.duoc.band_world.data.DatabaseInitializer
import cl.duoc.band_world.functions.SessionFunctions
import cl.duoc.band_world.ui.components.addWidth
import cl.duoc.band_world.ui.navigation.AppBottomBar
import cl.duoc.band_world.ui.navigation.AppNavHost
import cl.duoc.band_world.ui.navigation.Routes
import cl.duoc.band_world.ui.theme.BandWorldTheme
import kotlinx.coroutines.launch

// imports..

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = BandWorldDatabase.getDatabase(this)
//        val sessionFunctions = SessionFunctions(this)

        lifecycleScope.launch {
            DatabaseInitializer.initializeDatabase(database)
            enableEdgeToEdge()
            setContent {
                BandWorldTheme() {
                    BandWorldApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun BandWorldApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val startDestination = Routes.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Band World",
                            style = MaterialTheme.typography.titleMedium
                        )

                        addWidth(2.dp)

                        Badge(
                            modifier = Modifier.offset(y = (-2).dp),
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = Color.White,
                        ) {
                            Text(
                                text = "v. ALPHA",
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .9f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedDestination = selectedDestination,
                onSelected = { selectedDestination= it },
                navController = navController
            )
        }
    ) { innerPadding ->
        AppNavHost(navController, startDestination, modifier = Modifier.padding(innerPadding ))
    }
}



