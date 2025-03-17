package tech.gregbuilds.barrowstodoapp.ui.details.state

import tech.gregbuilds.barrowstodoapp.model.TodoItem

sealed class TodoDetailsUiState {
    data object Empty : TodoDetailsUiState()
    data object Loading : TodoDetailsUiState()
    data class Failed(val errorMessage: String) : TodoDetailsUiState()
    data class Success(val todoItem: TodoItem? = null) : TodoDetailsUiState()
}
