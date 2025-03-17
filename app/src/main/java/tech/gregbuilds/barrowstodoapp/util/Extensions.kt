package tech.gregbuilds.barrowstodoapp.util

import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
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
        iconIdentifier = iconIdentifier,
        daysUntilDueDisplay = daysUntilDue.daysUntilDueDisplay()
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
        body = this.body,
        title = this.title,
        dueDateLong = this.dueDate,
        completed = this.isCompleted,
        dueDateString = this.dueDateString,
        iconIdentifier = this.iconIdentifier
    )
}