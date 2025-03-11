package tech.gregbuilds.barrowstodoapp.ui.listScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.gregbuilds.barrowstodoapp.model.TodoItemDTO
import tech.gregbuilds.barrowstodoapp.ui.listScreen.state.TodoListUiState
import javax.inject.Inject

//TODO think about whether I need a baseViewModel or not.
@HiltViewModel
class TodoViewModel @Inject constructor(
    //TODO inject repository and have it interact with our Room database; also think about state handling.
//    private val state: TodoListUiState(),
//    private val repository: TodoRepository
) : ViewModel() {

    //TODO clean up my state and how I update it.
    private val _uiState = MutableStateFlow(TodoListUiState())
    val uiState: StateFlow<TodoListUiState> get() = _uiState.asStateFlow()

    private val _todoItems = MutableStateFlow<List<TodoItemDTO>>(emptyList())
    val todoItems: StateFlow<List<TodoItemDTO>> get() = _todoItems.asStateFlow()

    init {
        getTodoItems()
    }

    private fun updateItem() {}

    //TODO nice to have - is a bonus
    private fun swipeToDeleteItem() {}

    private fun getTodoItems() {
        viewModelScope.launch {
        }
    }
}