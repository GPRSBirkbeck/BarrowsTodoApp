package tech.gregbuilds.barrowstodoapp.navigation

sealed class NavigationItem(val route: String) {
    data object TodoListScreen : NavigationItem("todoListScreen")
    data class TodoDetailScreen(val id: Int? = null) : NavigationItem(
        if (id != null) "todoDetailScreen/$id" else "todoDetailScreen"
    ) {
        companion object {
            //TODO improve on this naming..
            const val ROUTEWITHID = "todoDetailScreen/{id}"
            const val ROUTEWITHOUTID = "todoDetailScreen"
            fun createRoute(id: Int?) = if (id != null) "todoDetailScreen/$id" else "todoDetailScreen"
        }
    }
}