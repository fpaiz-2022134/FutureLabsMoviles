
package plat.futurelabs.paiz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import plat.futurelabs.paiz.screens.login.LoginRoute
import plat.futurelabs.paiz.screens.login.StartupScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = StartupDestination
    ) {

        composable<StartupDestination> {
            StartupScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination) {
                        popUpTo<StartupDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(MainDestination) {
                        popUpTo<StartupDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<LoginDestination> {
            LoginRoute(
                onLoginSuccessful = {
                    navController.navigate(MainDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

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
