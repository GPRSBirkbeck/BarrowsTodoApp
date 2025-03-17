package tech.gregbuilds.barrowstodoapp.ui.list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.gregbuilds.barrowstodoapp.data.repositories.TodoRepositoryImpl
import tech.gregbuilds.barrowstodoapp.ui.list.state.TodoListUiState
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Loading)
    val uiState: StateFlow<TodoListUiState> get() = _uiState.asStateFlow()

    init {
        getTodoItems()
    }

    private fun getTodoItems() {
        viewModelScope.launch {
            try {
                val items = todoRepository.getTodoItems()
                if (items.isEmpty()) {
                    _uiState.value = TodoListUiState.Empty
                } else {
                    _uiState.value = TodoListUiState.Success(items)
                }
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Failed("Failed to load items: ${e.message}")
            }
        }
    }

    fun swipeToDeleteItem(id: Int) {
        viewModelScope.launch {
            todoRepository.deleteTodoItem(id)
            getTodoItems()
        }
    }

    fun onScreenResumed() {
        refreshData()
    }

    // kept as a separate function to allow expansion in the future - it does look like duplication for now - sorry.
    private fun refreshData() {
        getTodoItems()
    }
}