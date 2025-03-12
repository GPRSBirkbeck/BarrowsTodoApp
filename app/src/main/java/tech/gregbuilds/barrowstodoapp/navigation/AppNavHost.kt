package tech.gregbuilds.barrowstodoapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.gregbuilds.barrowstodoapp.ui.listScreen.screen.TodoListScreen
import tech.gregbuilds.barrowstodoapp.ui.listScreen.viewModel.TodoViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    //TODO add navigation actions as a separate sealed class.
    NavHost(
        navController = navController,
        startDestination = NavigationItem.TodoListScreen.route
    ) {
        composable(NavigationItem.TodoListScreen.route) {
            // I'm obtaining an instance of the viewModel here, and not in the screen, are what the hilt & navigation & viewModel docs recommend [here](https://developer.android.com/develop/ui/compose/libraries#hilt-navigation)
            val viewModel = hiltViewModel<TodoViewModel>()
            TodoListScreen(viewModel)
        }
    }
}