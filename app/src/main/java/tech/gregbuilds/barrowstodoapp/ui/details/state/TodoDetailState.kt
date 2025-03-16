package tech.gregbuilds.barrowstodoapp.ui.details.state

import tech.gregbuilds.barrowstodoapp.model.TodoItem

sealed class TodoDetailsUiState {
    object Loading : TodoDetailsUiState()
    data class Success(val todoItem: TodoItem? = null) : TodoDetailsUiState()
    data class Failed(val errorMessage: String) : TodoDetailsUiState()
    object Empty : TodoDetailsUiState()
}

//TODO extracts
// New sealed class for navigation events
sealed class NavigationEvent {
    object NavigateBack : NavigationEvent()
}