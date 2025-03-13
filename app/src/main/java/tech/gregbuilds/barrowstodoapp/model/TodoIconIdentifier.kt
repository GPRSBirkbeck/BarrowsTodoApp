package tech.gregbuilds.barrowstodoapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

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