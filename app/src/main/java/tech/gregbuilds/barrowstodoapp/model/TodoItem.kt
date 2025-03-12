package tech.gregbuilds.barrowstodoapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

// Kept this separate from the database model to separate concerns and improve testability.
// Also allows for changes if needed it the future
data class TodoItem(
    val id: Int,
    val title: String,
    val body: String,
    val isCompleted: Boolean,
    val daysUntilDue: Int,
    val dueDateString: String,
    val daysUntilDueDisplay: String,
    val icon: ImageVector // TODO think about if I'm happy with this, or want icon here - will impact mapper.
)

// This could live in the utils package, but I'm keeping it here for now
sealed class TodoIconIdentifier(val identifier: String, val icon: ImageVector) {
    data object Check : TodoIconIdentifier("check", Icons.Filled.Check)
    data object Date : TodoIconIdentifier("date", Icons.Filled.DateRange)
    data object Info : TodoIconIdentifier("info", Icons.Filled.Info)

    companion object {
        fun fromIdentifier(identifier: String): TodoIconIdentifier {
            return when (identifier) {
                Check.identifier -> Check
                Date.identifier -> Date
                else -> Info
            }
        }
    }
}