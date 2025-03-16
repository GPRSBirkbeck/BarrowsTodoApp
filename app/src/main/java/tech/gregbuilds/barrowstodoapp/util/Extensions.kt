package tech.gregbuilds.barrowstodoapp.util

import tech.gregbuilds.barrowstodoapp.common.Constants.DATE_PATTERN
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoIconIdentifier
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

//This extension function could equally live in the model directory.
fun TodoItemEntity.toTodoItemUi(): TodoItem {
    val dueDate = Instant.ofEpochMilli(dueDateLong)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val daysUntilDue = LocalDate.now().until(dueDate, ChronoUnit.DAYS).toInt()

    return TodoItem(
        id = id,
        body = body,
        title = title,
        dueDate = dueDateLong,
        isCompleted = completed,
        daysUntilDue = daysUntilDue,
        dueDateString = dueDateString,
        daysUntilDueDisplay = daysUntilDue.daysUntilDueDisplay(),
        icon = TodoIconIdentifier.fromIdentifier(iconIdentifier).icon
    )
}

fun Int.daysUntilDueDisplay(): String {
    return when {
        this < 0 -> "Overdue"
        this == 0 -> "Due Today"
        this == 1 -> "Due Tomorrow"
        else -> "$this days"
    }
}

// Extension function to convert TodoItem to TodoItemEntity
fun TodoItem.toTodoItemEntity(): TodoItemEntity {
    return TodoItemEntity(
        title = this.title,
        body = this.body,
        completed = this.isCompleted,
        dueDateLong = this.dueDate,
        dueDateString = this.dueDateString,
        iconIdentifier = this.icon.name
    )
}