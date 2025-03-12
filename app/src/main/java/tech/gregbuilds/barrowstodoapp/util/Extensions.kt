package tech.gregbuilds.barrowstodoapp.util

import tech.gregbuilds.barrowstodoapp.common.Constants.DATE_PATTERN
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoIconIdentifier
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun TodoItemEntity.toTodoItemUi(dateFormatterService: DateFormatterService): TodoItem {
    val dueDate = Instant.ofEpochMilli(dueDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val daysUntilDue = LocalDate.now().until(dueDate, ChronoUnit.DAYS).toInt()
    val formattedDate = dateFormatterService.formatDate(dueDate.toEpochDay(), DATE_PATTERN)

    return TodoItem(
        id = id,
        title = title,
        body = body,
        isCompleted = completed,
        daysUntilDue = daysUntilDue,
        dueDateString = formattedDate,
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