package tech.gregbuilds.barrowstodoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.gregbuilds.barrowstodoapp.ui.listScreen.screen.TodoListScreen

//TODO implement the correct routes for this. I might not even want a nav host and just have the details screen be a composable - not sure..
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "todoListScreen") {
        composable("todoListScreen") { TodoListScreen() }
    }
}