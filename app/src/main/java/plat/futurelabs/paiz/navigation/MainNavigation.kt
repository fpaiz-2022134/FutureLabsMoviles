package plat.futurelabs.paiz.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import plat.futurelabs.paiz.screens.profile.ProfileScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import plat.futurelabs.paiz.screens.characters.CharacterDetailRoute
import plat.futurelabs.paiz.screens.characters.CharactersRoute
import plat.futurelabs.paiz.screens.locations.LocationsRoute
import plat.futurelabs.paiz.screens.locations.LocationDetailRoute
@Composable
fun MainNavigation(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    // Vemos cuál es el destino actual.
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    /*
     * Determinamos en qué sección principal estamos.
     *
     * hierarchy incluye tanto el destino actual como el nested graph
     * al que pertenece.
     */
    val selectedItem = when {

        currentDestination?.hierarchy?.any {
            it.route == CharactersGraph::class.qualifiedName
        } == true -> {
            BottomNavigationItem.CHARACTERS
        }

        currentDestination?.hierarchy?.any {
            it.route == LocationsGraph::class.qualifiedName
        } == true -> {
            BottomNavigationItem.LOCATIONS
        }

        currentDestination?.route ==
                ProfileDestination::class.qualifiedName -> {
            BottomNavigationItem.PROFILE
        }

        else -> {
            BottomNavigationItem.CHARACTERS
        }
    }


    val showBottomBar =
        currentDestination?.route ==
                CharactersDestination::class.qualifiedName ||
                currentDestination?.route ==
                LocationsDestination::class.qualifiedName ||
                currentDestination?.route ==
                ProfileDestination::class.qualifiedName

    Scaffold(
        modifier = modifier.fillMaxSize(),

        bottomBar = {

            if (showBottomBar) {

                BottomNavigationBar(
                    selectedItem = selectedItem,

                    onCharactersClick = {

                        navController.navigate(
                            CharactersGraph
                        ) {

                            popUpTo(
                                navController.graph.startDestinationId
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    onLocationsClick = {

                        navController.navigate(
                            LocationsGraph
                        ) {

                            popUpTo(
                                navController.graph.startDestinationId
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    onProfileClick = {

                        navController.navigate(
                            ProfileDestination
                        ) {

                            popUpTo(
                                navController.graph.startDestinationId
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Characters nested graph

            navigation<CharactersGraph>(
                startDestination = CharactersDestination
            ) {

                composable<CharactersDestination> {

                    CharactersRoute(
                        onCharacterClick = { characterId ->

                            navController.navigate(
                                CharacterDetailDestination(
                                    id = characterId
                                )
                            )
                        }
                    )
                }

                composable<CharacterDetailDestination> {
                        backStackEntry ->

                    val destination =
                        backStackEntry
                            .toRoute<CharacterDetailDestination>()

                    CharacterDetailRoute(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }


            // Locations nested graph

            navigation<LocationsGraph>(
                startDestination = LocationsDestination
            ) {

                composable<LocationsDestination> {

                    LocationsRoute(
                        onLocationClick = { id ->
                            navController.navigate(
                                LocationDetailDestination(id)
                            )
                        }
                    )
                }

                composable<LocationDetailDestination> {
                        backStackEntry ->

                    val destination =
                        backStackEntry
                            .toRoute<LocationDetailDestination>()

                    LocationDetailRoute(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }


            // Profile

            composable<ProfileDestination> {

                ProfileScreen(
                    onLogoutClick = onLogout
                )
            }
        }
    }
}