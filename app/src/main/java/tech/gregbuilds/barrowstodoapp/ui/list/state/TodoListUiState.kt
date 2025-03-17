package tech.gregbuilds.barrowstodoapp.ui.list.state

import tech.gregbuilds.barrowstodoapp.model.TodoItem

sealed class TodoListUiState {
    data object Empty : TodoListUiState()
    data object Loading : TodoListUiState()
    data class Failed(val errorMessage: String) : TodoListUiState()
    data class Success(val listItems: List<TodoItem>) : TodoListUiState()
}