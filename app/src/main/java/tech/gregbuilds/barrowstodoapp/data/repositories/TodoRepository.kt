package tech.gregbuilds.barrowstodoapp.data.repositories

import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoItem

interface TodoRepository {
    suspend fun getTodoItems(): List<TodoItem>

    suspend fun getTodoItemById(id: Int): TodoItem

    suspend fun updateTodoItem(todoItemEntity: TodoItemEntity)

    suspend fun insertTodoItem(todoItemEntity: TodoItemEntity)

    suspend fun deleteTodoItem(todoItemEntity: TodoItemEntity)

}

