package co.future.exerciseprogress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import co.future.exerciseprogress.ui.clientdetail.ClientDetailScreen
import co.future.exerciseprogress.ui.components.ScreenScaffold
import co.future.exerciseprogress.ui.previousworkouts.PreviousWorkoutsScreen
import co.future.exerciseprogress.ui.welcome.WelcomeScreen
import co.future.exerciseprogress.ui.workoutdetail.WorkoutDetailScreen

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

        composable<ClientDetail> { backStackEntry ->
            val clientID = backStackEntry.toRoute<ClientDetail>().clientID

            ScreenScaffold { contentPadding ->
                ClientDetailScreen(
                    contentPadding = contentPadding,
                    onPreviousWorkoutsClick = { navController.navigate(PreviousWorkouts(clientID)) }
                )
            }
        }

        composable<PreviousWorkouts> {
            ScreenScaffold { contentPadding ->
                PreviousWorkoutsScreen(
                    contentPadding = contentPadding,
                    onWorkoutClick = { workoutID -> navController.navigate(WorkoutDetail(workoutID)) }
                )
            }
        }

        composable<WorkoutDetail> {
            ScreenScaffold { contentPadding ->
                WorkoutDetailScreen(contentPadding = contentPadding)
            }
        }
    }
}
