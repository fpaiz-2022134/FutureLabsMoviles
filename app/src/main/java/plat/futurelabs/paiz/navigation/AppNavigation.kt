
package plat.futurelabs.paiz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import plat.futurelabs.paiz.screens.login.LoginScreen
import plat.futurelabs.paiz.screens.login.StartupScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = StartupDestination,
        modifier = modifier
    ) {

        // Verificar sesión almacenada

        composable<StartupDestination> {
            StartupScreen(
                onSessionFound = {
                    navController.navigate(MainDestination) {
                        popUpTo<StartupDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onSessionNotFound = {
                    navController.navigate(LoginDestination) {
                        popUpTo<StartupDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Login

        composable<LoginDestination> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(MainDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Aplicación principal

        composable<MainDestination> {
            MainNavigation(
                onLogout = {
                    navController.navigate(LoginDestination) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
