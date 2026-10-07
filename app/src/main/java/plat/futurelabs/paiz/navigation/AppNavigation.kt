package plat.futurelabs.paiz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import plat.futurelabs.paiz.screens.login.LoginScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = modifier
    ) {

        composable<LoginDestination> {

            LoginScreen(
                onStartClick = {
                    navController.navigate(MainDestination) {

                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<MainDestination> {

            MainNavigation(
                onLogout = {
                    navController.navigate(LoginDestination) {

                        popUpTo(0)
                    }
                }
            )
        }
    }
}