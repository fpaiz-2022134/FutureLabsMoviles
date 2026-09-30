package plat.futurelabs.paiz.AppNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.futurelabs.paiz.screens.characters.CharactersScreen
import plat.futurelabs.paiz.screens.detail.CharacterDetailScreen
import plat.futurelabs.paiz.screens.login.LoginScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = modifier
    ){
        composable <LoginDestination>{
            LoginScreen(
                onStartClick = {
                        navController.navigate(
                            route = CharactersDestination
                        ){
                            popUpTo <LoginDestination>{
                                inclusive = true
                            }
                        }
                }
            )
        }
        composable <CharactersDestination>{
            CharactersScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(
                        route = CharacterDetailDestination(
                            id = characterId
                        )
                    )

                }
            )
        }

        composable<CharacterDetailDestination> { backStackEntry ->

            val destination: CharacterDetailDestination =
                backStackEntry.toRoute()

            CharacterDetailScreen(
                characterId = destination.id,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}