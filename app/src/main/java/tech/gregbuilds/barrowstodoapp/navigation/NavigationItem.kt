package tech.gregbuilds.barrowstodoapp.navigation

sealed class NavigationItem(val route: String) {
    data object TodoListScreen : NavigationItem("todoListScreen")
    data class TodoDetailScreen(val id: Int? = null) : NavigationItem(
        if (id != null) "todoDetailScreen/$id" else "todoDetailScreen"
    ) {
        companion object {
            const val ROUTE_WITH_ID = "todoDetailScreen/{id}"
            const val ROUTE_WITHOUT_ID = "todoDetailScreen"
            fun createRoute(id: Int?) = if (id != null) "todoDetailScreen/$id" else "todoDetailScreen"
        }
    }
}