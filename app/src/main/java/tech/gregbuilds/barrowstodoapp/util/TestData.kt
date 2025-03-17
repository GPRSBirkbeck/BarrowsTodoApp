package tech.gregbuilds.barrowstodoapp.util

import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoIconIdentifier

object TestData {
    fun getTodoItems(): List<TodoItemEntity> {
        return listOf(
            TodoItemEntity(
                title = "Work meeting about printing press",
                body = "Talk to Benjamin about the new printing press",
                dueDateString = "Overdue",
                dueDateLong = System.currentTimeMillis() - 86400000, // Yesterday
                iconIdentifier = TodoIconIdentifier.Cart.name,
                completed = false,
            ),
            TodoItemEntity(
                title = "Grocery Shopping",
                body = "Buy milk, eggs, bread, and cheese",
                dueDateLong = System.currentTimeMillis() + 86400000, // Tomorrow
                dueDateString = "Tomorrow",
                iconIdentifier = TodoIconIdentifier.Cart.name,
                completed = false,
            ),
            TodoItemEntity(
                completed = false,
                title = "Finish Lord of the Rings",
                body = "By next week, finish reading the trilogy",
                dueDateString = "21st March 2025",
                dueDateLong = System.currentTimeMillis() + 604800000, // Next week
                iconIdentifier = TodoIconIdentifier.Book.name
            ),
            TodoItemEntity(
                completed = false,
                title = "Buy briefcase",
                dueDateString = "20th March 2025",
                body = "Head to Oxford Street for a new briefcase",
                dueDateLong = System.currentTimeMillis() + 259200000, // 3 days from now
                iconIdentifier = TodoIconIdentifier.Work.name
            ),
            TodoItemEntity(
                completed = false,
                title = "Book electrician",
                body = "Write back to the electrician to confirm the appointment",
                dueDateString = "19th March 2025",
                dueDateLong = System.currentTimeMillis() + 172800000, // 2 days from now
                iconIdentifier = TodoIconIdentifier.Home.name
            ),
            TodoItemEntity(
                completed = false,
                title = "Reminder",
                body = "Take Sparky for a walk in the park",
                dueDateString = "25th March 2025",
                dueDateLong = System.currentTimeMillis() + 604800000, // next week
                iconIdentifier = TodoIconIdentifier.Alarm.name
            )
        )
    }
}