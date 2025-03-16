package tech.gregbuilds.barrowstodoapp.model

import androidx.compose.ui.graphics.vector.ImageVector

// Kept this separate from the database model to separate concerns and improve testability.
// Also allows for changes if needed it the future
data class TodoItem(
    val id: Int,
    val title: String,
    val body: String,
    val dueDate: Long,
    val daysUntilDue: Int,
    val isCompleted: Boolean,
    val dueDateString: String,
    val iconIdentifier: String,
    val daysUntilDueDisplay: String
)