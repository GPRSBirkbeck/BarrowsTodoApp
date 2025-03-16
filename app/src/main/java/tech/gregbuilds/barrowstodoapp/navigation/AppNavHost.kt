package tech.gregbuilds.barrowstodoapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.gregbuilds.barrowstodoapp.ui.details.screen.TodoDetailScreen
import tech.gregbuilds.barrowstodoapp.ui.details.viewModel.TodoDetailViewModel
import tech.gregbuilds.barrowstodoapp.ui.list.screen.TodoListScreen
import tech.gregbuilds.barrowstodoapp.ui.list.viewModel.TodoListViewModel

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
            //TODO look at navController.popBackStack() instead of having a bespoke action for this.
            val viewModel = hiltViewModel<TodoDetailViewModel>()
            val id = backStackEntry.arguments?.getString("id")?.toInt()
            TodoDetailScreen(todoId = id, viewModel = viewModel, goBack = { navController.popBackStack() })
        }
        composable(NavigationItem.TodoDetailScreen.ROUTEWITHOUTID) {
            //TODO tidy up double instance getting of this viewModel.
            val viewModel = hiltViewModel<TodoDetailViewModel>()
            TodoDetailScreen(todoId = null, viewModel = viewModel, goBack = { navController.popBackStack() })
        }
    }
}