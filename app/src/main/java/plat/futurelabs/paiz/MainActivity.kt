package plat.futurelabs.paiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import plat.futurelabs.paiz.navigation.AppNavigation
import plat.futurelabs.paiz.ui.theme.FutureLabsMovilesTheme

/*
*@author Franco Paiz 25780
*
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FutureLabsMovilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

