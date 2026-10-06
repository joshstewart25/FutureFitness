package co.future.exerciseprogress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.future.exerciseprogress.ui.clientdetail.ClientDetailScreen
import co.future.exerciseprogress.ui.components.ScreenScaffold
import co.future.exerciseprogress.ui.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Welcome) {
        composable<Welcome> {
            ScreenScaffold { contentPadding ->
                WelcomeScreen(
                    contentPadding = contentPadding,
                    onClientClick = { clientID -> navController.navigate(ClientDetail(clientID)) }
                )
            }
        }

        composable<ClientDetail> {
            ScreenScaffold { contentPadding ->
                ClientDetailScreen(contentPadding = contentPadding)
            }
        }
    }
}
