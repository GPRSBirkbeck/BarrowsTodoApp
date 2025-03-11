package tech.gregbuilds.barrowstodoapp.model

import androidx.compose.material.icons.Icons
import java.util.Date

//TODO update with correct DTO
data class TodoItemDTO(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val completed: Boolean,
    val dueDate: Date, //TODO see if there's a different date object I'd like to use.
    val icon: Icons
)