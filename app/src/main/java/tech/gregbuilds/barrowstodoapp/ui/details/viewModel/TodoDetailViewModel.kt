package tech.gregbuilds.barrowstodoapp.ui.details.viewModel

import java.util.concurrent.TimeUnit
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
import tech.gregbuilds.barrowstodoapp.util.toTodoItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(private val todoRepository: TodoRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<TodoDetailsUiState>(TodoDetailsUiState.Loading)
    val uiState: StateFlow<TodoDetailsUiState> = _uiState.asStateFlow()

    // New SharedFlow for navigation events
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // State variables
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

    private var todoId: Int? = null

    fun loadTodoItem(todoId: Int?) {
        this.todoId = todoId
        viewModelScope.launch {
            _uiState.value = TodoDetailsUiState.Loading
            if (todoId == null) {
                _uiState.value = TodoDetailsUiState.Success()
            } else {
                val todoItem = todoRepository.getTodoItemById(todoId)
                _title.value = todoItem.title
                _body.value = todoItem.body
                _selectedIcon.value = TodoIconIdentifier.fromImageVector(todoItem.icon)
                _selectedDate.value = todoItem.dueDate
                _uiState.value = TodoDetailsUiState.Success(todoItem)
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
        _dueDateString.value = getFormattedDate(newDate)
        updateSaveEnabled()
    }

    private fun updateSaveEnabled() {
        _isSaveEnabled.value = _title.value.isNotBlank() && _body.value.isNotBlank() && _selectedDate.value != null
    }

    //TODO add use cases
    fun saveTodoItem() {
        viewModelScope.launch {
            val todoItem = TodoItem(
                id = todoId ?: 0,
                title = title.value,
                body = body.value,
                dueDate = selectedDate.value ?: 0,
                dueDateString = dueDateString.value,
                daysUntilDueDisplay = getDaysUntilDueDisplay(selectedDate.value),
                icon = selectedIcon.value.icon,
                daysUntilDue = getDaysUntilDue(selectedDate.value).toInt(),
                isCompleted = false
            )
            if (todoId == null && todoId != 0) {
                todoRepository.insertTodoItem(todoItem.toTodoItemEntity())
            } else {
                todoRepository.updateTodoItem(todoItem.toTodoItemEntity())
            }
            // Emit a navigation event after successful save
            _navigationEvents.emit(NavigationEvent.NavigateBack)
        }
    }

    private fun getFormattedDate(date: Long?): String {
        if (date == null) return ""
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) //extract this ofc to a dependency.
        return dateFormat.format(Date(date))
    }

    private fun getDaysUntilDueDisplay(date: Long?): String {
        if (date == null) return ""
        val today = Date()
        val dueDate = Date(date)
        val diff = dueDate.time - today.time
        val days = diff / (1000 * 60 * 60 * 24)
        return when {
            days < 0 -> "Overdue"
            days == 0L -> "Due Today"
            days == 1L -> "Due Tomorrow"
            else -> "Due in $days days"
        }
    }

    private fun getDaysUntilDue(date: Long?): Long {
        if (date == null) return Long.MAX_VALUE // Or another appropriate value for null dates
        val today = Date()
        val dueDate = Date(date)
        val diffInMillis = dueDate.time - today.time
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }
}