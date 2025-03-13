package tech.gregbuilds.barrowstodoapp.ui.list.state

import tech.gregbuilds.barrowstodoapp.model.TodoItem

sealed class TodoListUiState {
    data object Loading : TodoListUiState()
    data class Success(val listItems: List<TodoItem>) : TodoListUiState()
    data class Failed(val errorMessage: String) : TodoListUiState()
    data object Empty : TodoListUiState()
}