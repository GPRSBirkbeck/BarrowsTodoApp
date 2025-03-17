package tech.gregbuilds.barrowstodoapp.ui.list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.gregbuilds.barrowstodoapp.data.repositories.TodoRepositoryImpl
import tech.gregbuilds.barrowstodoapp.ui.list.state.SortType
import tech.gregbuilds.barrowstodoapp.ui.list.state.TodoListUiState
import tech.gregbuilds.barrowstodoapp.util.TodoNotificationServiceImpl
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepositoryImpl,
    private val todoNotificationServiceImpl: TodoNotificationServiceImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Loading)
    val uiState: StateFlow<TodoListUiState> get() = _uiState.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.NONE)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var hasSentNotification = false

    init {
        getTodoItems()
    }

    private fun getTodoItems(ascending: Boolean? = null) {
        viewModelScope.launch {
            try {
                val items = if (searchQuery.value.isNotEmpty() && sortType.value == SortType.NONE) {
                    todoRepository.searchTodoItems(searchQuery.value)
                } else if (ascending != null) {
                    todoRepository.getTodoItemsOrderedByWordFrequency(searchQuery.value, ascending)
                } else {
                    todoRepository.getTodoItems()
                }
                if (items.isEmpty() && searchQuery.value.isEmpty()) {
                    _uiState.value = TodoListUiState.Empty
                } else if (items.isEmpty()) {
                    _uiState.value = TodoListUiState.None
                } else {
                    val itemsDueToday = items.filter { it.daysUntilDue == 0 }
                    if (itemsDueToday.isNotEmpty()) {
                        val firstItemDueToday = itemsDueToday.first()
                        todoNotificationServiceImpl.showNotification(firstItemDueToday)
                        hasSentNotification = true // A much better solution would be implemented in the long run.
                    }
                    _uiState.value = TodoListUiState.Success(items)
                }
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Failed("Failed to load items: ${e.message}")
            }
        }
    }

    fun cycleSortType() {
        viewModelScope.launch {
            _sortType.value = when (sortType.value) {
                SortType.NONE -> SortType.ASCENDING
                SortType.ASCENDING -> SortType.DESCENDING
                SortType.DESCENDING -> SortType.NONE
            }
            if (sortType.value != SortType.NONE) {
                getTodoItems()
            } else {
                getTodoItems(ascending = sortType.value == SortType.ASCENDING)
            }
        }
    }

    fun updateSearchText(searchText: String) {
        _searchQuery.value = searchText
        getTodoItems(ascending =  null)
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

    fun addTestData() {
        viewModelScope.launch {
            todoRepository.addTestData()
            getTodoItems()
        }
    }

    // Kept as a separate function to allow expansion in the future - it does look like duplication for now - sorry.
    private fun refreshData() {
        getTodoItems()
    }
}