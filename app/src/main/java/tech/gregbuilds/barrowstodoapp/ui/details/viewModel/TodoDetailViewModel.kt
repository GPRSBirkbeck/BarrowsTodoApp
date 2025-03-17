package tech.gregbuilds.barrowstodoapp.ui.details.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.gregbuilds.barrowstodoapp.data.repositories.TodoRepository
import tech.gregbuilds.barrowstodoapp.model.TodoIconIdentifier
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import tech.gregbuilds.barrowstodoapp.ui.details.state.NavigationEvent
import tech.gregbuilds.barrowstodoapp.ui.details.state.TodoDetailsUiState
import tech.gregbuilds.barrowstodoapp.util.DateFormatterService
import tech.gregbuilds.barrowstodoapp.util.toTodoItemEntity
import tech.gregbuilds.barrowstodoapp.util.toTodoItemEntityWithId
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val dateFormatterService: DateFormatterService
) : ViewModel() {

    //Our state object for the UI
    private val _uiState = MutableStateFlow<TodoDetailsUiState>(TodoDetailsUiState.Loading)
    val uiState: StateFlow<TodoDetailsUiState> = _uiState.asStateFlow()

    // Navigation event states - for now only used to trigger a back navigation
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // State objects for the form fields - debatably these could be combined into the uiState object - but this feels cleaner when it comes to updates.
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _body = MutableStateFlow("")
    val body: StateFlow<String> = _body.asStateFlow()

    private val _selectedIcon = MutableStateFlow(TodoIconIdentifier.Alarm)
    val selectedIcon: StateFlow<TodoIconIdentifier> = _selectedIcon.asStateFlow()

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    private val _isSaveEnabled = MutableStateFlow(false)
    val isSaveEnabled: StateFlow<Boolean> = _isSaveEnabled.asStateFlow()

    private val _dueDateString = MutableStateFlow("")
    val dueDateString: StateFlow<String> = _dueDateString.asStateFlow()


    var isExistingTodo: Boolean by mutableStateOf(false)
    private var selectedTodoId: Int? = null

    fun loadTodoItem(todoId: Int?) {
        selectedTodoId = todoId
        viewModelScope.launch {
            _uiState.value = TodoDetailsUiState.Loading
            if (todoId == null) {
                _uiState.value = TodoDetailsUiState.Success()
            } else {
                isExistingTodo = true
                val todoItem = todoRepository.getTodoItemById(todoId)
                _title.value = todoItem.title
                _body.value = todoItem.body
                _selectedDate.value = todoItem.dueDate
                _dueDateString.value = todoItem.dueDateString
                _uiState.value = TodoDetailsUiState.Success(todoItem)
                _selectedIcon.value = TodoIconIdentifier.fromIdentifier(todoItem.iconIdentifier)
            }
            updateSaveEnabled()
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
        TodoDetailsUiState.Success().let {
            _uiState.value = it
        }
        updateSaveEnabled()
    }

    fun updateBody(newBody: String) {
        _body.value = newBody
        updateSaveEnabled()
    }

    fun updateSelectedIcon(newIcon: TodoIconIdentifier) {
        _selectedIcon.value = newIcon
    }

    fun updateSelectedDate(newDate: Long?) {
        _selectedDate.value = newDate
        _dueDateString.value = dateFormatterService.getFormattedDate(newDate)
        updateSaveEnabled()
    }

    private fun updateSaveEnabled() {
        _isSaveEnabled.value = title.value.isNotBlank()
                    && body.value.isNotBlank()
                    && selectedDate.value != null
    }

    fun deleteTodoItem() {
        selectedTodoId?.let {
            viewModelScope.launch {
                todoRepository.deleteTodoItem(it)
                _navigationEvents.emit(NavigationEvent.NavigateBack)
            }
        }
    }

    //In a production project I would add use cases for our domain layer.
    fun saveTodoItem() {
        viewModelScope.launch {
            val todoItem = TodoItem(
                id = selectedTodoId ?: 0,
                body = body.value,
                isCompleted = false,
                title = title.value,
                dueDate = selectedDate.value ?: 0,
                dueDateString = dueDateString.value,
                iconIdentifier = selectedIcon.value.name,
                daysUntilDue = dateFormatterService.getDaysUntilDue(selectedDate.value).toInt(),
                daysUntilDueDisplay = dateFormatterService.getDaysUntilDueDisplay(selectedDate.value)
            )
            if (selectedTodoId == null) {
                todoRepository.insertTodoItem(todoItem.toTodoItemEntity())
            } else {
                todoRepository.updateTodoItem(todoItem.toTodoItemEntityWithId())
            }
            // Emit a navigation event after successful save
            _navigationEvents.emit(NavigationEvent.NavigateBack)
        }
    }
}