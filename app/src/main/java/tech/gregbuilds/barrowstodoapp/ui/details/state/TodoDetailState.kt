package tech.gregbuilds.barrowstodoapp.ui.details.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import tech.gregbuilds.barrowstodoapp.model.TodoItem

data class TodoDetailState(
    val title: String = "",
    val body: String = "",
    val dueDate: Long = 0,
    val daysUntilDue: Int = 0,
    val dueDateString: String = "",
    val isCompleted: Boolean = false,
    val existingItem: TodoItem? = null,
    val icon: ImageVector = Icons.Default.Info
)