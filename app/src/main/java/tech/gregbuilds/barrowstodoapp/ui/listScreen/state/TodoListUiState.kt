package tech.gregbuilds.barrowstodoapp.ui.listScreen.state

import tech.gregbuilds.barrowstodoapp.model.TodoItem

data class TodoListUiState(
    val listItems: List<TodoItem> = emptyList()
)