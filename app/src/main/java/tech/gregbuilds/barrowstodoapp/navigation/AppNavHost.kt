package tech.gregbuilds.barrowstodoapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.gregbuilds.barrowstodoapp.ui.list.screen.TodoListScreen
import tech.gregbuilds.barrowstodoapp.ui.list.viewModel.TodoListViewModel
import tech.gregbuilds.barrowstodoapp.ui.details.screen.TodoDetailScreen
import tech.gregbuilds.barrowstodoapp.ui.details.viewModel.TodoDetailViewModel

//For deeplinks, this is a good refresher: https://developer.android.com/develop/ui/compose/navigation#deeplinks
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.TodoListScreen.route
    ) {
        composable(NavigationItem.TodoListScreen.route) {
            val viewModel = hiltViewModel<TodoListViewModel>()
            TodoListScreen(
                viewModel = viewModel,
                onItemClicked = { id ->
                    navController.navigate(
                        NavigationItem.TodoDetailScreen.createRoute(id)
                    )
                },
                onAddClicked = {
                    navController.navigate(
                        NavigationItem.TodoDetailScreen.createRoute(null)
                    )
                }
            )
        }
        composable(NavigationItem.TodoDetailScreen.ROUTEWITHID) { backStackEntry ->
            val viewModel = hiltViewModel<TodoDetailViewModel>()
            val id = backStackEntry.arguments?.getString("id")?.toInt()
            TodoDetailScreen(id = id, viewModel = viewModel)
        }
        composable(NavigationItem.TodoDetailScreen.ROUTEWITHOUTID) {
            //TODO tidy up double instance getting of this viewModel.
            val viewModel = hiltViewModel<TodoDetailViewModel>()
            TodoDetailScreen(id = null, viewModel = viewModel)
        }
    }
}