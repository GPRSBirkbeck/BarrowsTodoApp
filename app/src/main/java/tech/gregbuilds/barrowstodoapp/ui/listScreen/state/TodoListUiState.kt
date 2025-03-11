package tech.gregbuilds.barrowstodoapp.ui.listScreen.state

import tech.gregbuilds.barrowstodoapp.model.TodoItemDTO

data class TodoListUiState(
    val listItems: List<TodoItemDTO> = emptyList()
)