
package plat.futurelabs.paiz.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import plat.futurelabs.paiz.data.AppContainer
import plat.futurelabs.paiz.screens.characters.CharacterDetailRoute
import plat.futurelabs.paiz.screens.characters.CharacterDetailViewModel
import plat.futurelabs.paiz.screens.characters.CharacterFormScreen
import plat.futurelabs.paiz.screens.characters.CharacterFormViewModel
import plat.futurelabs.paiz.screens.characters.CharactersRoute
import plat.futurelabs.paiz.screens.characters.CharactersViewModel
import plat.futurelabs.paiz.screens.locations.LocationDetailRoute
import plat.futurelabs.paiz.screens.locations.LocationDetailViewModel
import plat.futurelabs.paiz.screens.locations.LocationsRoute
import plat.futurelabs.paiz.screens.locations.LocationsViewModel
import plat.futurelabs.paiz.screens.profile.ProfileRoute

@Composable
fun MainNavigation(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val container = remember(context) {
        AppContainer(context.applicationContext)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val selectedItem = when {
        currentDestination?.hierarchy?.any {
            it.hasRoute<LocationsGraph>()
        } == true -> BottomNavigationItem.LOCATIONS

        currentDestination?.hierarchy?.any {
            it.hasRoute<ProfileDestination>()
        } == true -> BottomNavigationItem.PROFILE

        else -> BottomNavigationItem.CHARACTERS
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,

                onCharactersClick = {
                    navController.navigate(CharactersGraph) {
                        popUpTo(CharactersGraph) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                onLocationsClick = {
                    navController.navigate(LocationsGraph) {
                        popUpTo(CharactersGraph) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                onProfileClick = {
                    navController.navigate(ProfileDestination) {
                        popUpTo(CharactersGraph) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(padding)
        ) {

            // CHARACTERS

            navigation<CharactersGraph>(
                startDestination = CharactersDestination
            ) {

                composable<CharactersDestination> {

                    val vm: CharactersViewModel = viewModel()

                    CharactersRoute(
                        viewModel = vm,

                        onCharacterClick = { id ->
                            navController.navigate(
                                CharacterDetailDestination(id)
                            )
                        },

                        onCreateCharacter = {
                            navController.navigate(
                                CharacterFormDestination()
                            )
                        },

                        onEditCharacter = { id ->
                            navController.navigate(
                                CharacterFormDestination(id)
                            )
                        }
                    )
                }

                composable<CharacterDetailDestination> {

                    val vm: CharacterDetailViewModel = viewModel()

                    CharacterDetailRoute(
                        viewModel = vm,

                        onBack = {
                            navController.popBackStack()
                        },

                        onEditCharacter = { id ->
                            navController.navigate(
                                CharacterFormDestination(id)
                            )
                        }
                    )
                }

                composable<CharacterFormDestination> { entry ->

                    val vm: CharacterFormViewModel = viewModel(
                        viewModelStoreOwner = entry,

                        factory = viewModelFactory {
                            initializer {
                                CharacterFormViewModel(
                                    repository = container.characterRepository,
                                    savedStateHandle = createSavedStateHandle()
                                )
                            }
                        }
                    )

                    CharacterFormScreen(
                        viewModel = vm,

                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            // LOCATIONS

            navigation<LocationsGraph>(
                startDestination = LocationsDestination
            ) {

                composable<LocationsDestination> {

                    val vm: LocationsViewModel = viewModel()

                    LocationsRoute(
                        viewModel = vm,

                        onLocationClick = { id ->
                            navController.navigate(
                                LocationDetailDestination(id)
                            )
                        }
                    )
                }

                composable<LocationDetailDestination> {

                    val vm: LocationDetailViewModel = viewModel()

                    LocationDetailRoute(
                        viewModel = vm,

                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            // PROFILE

            composable<ProfileDestination> {
                ProfileRoute(
                    onLogout = onLogout
                )
            }
        }
    }
}
