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

//TODO think about whether I need a baseViewModel or not.
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Loading)
    val uiState: StateFlow<TodoListUiState> get() = _uiState.asStateFlow()

    init {
        getTodoItems()
    }

    //TODO nice to have - is a bonus
    private fun swipeToDeleteItem() {}

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
}