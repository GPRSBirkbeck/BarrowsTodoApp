package tech.gregbuilds.barrowstodoapp.navigation

sealed class NavigationItem(val route: String) {
    data object TodoListScreen : NavigationItem("todoListScreen")
}