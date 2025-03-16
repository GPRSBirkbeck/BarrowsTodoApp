package tech.gregbuilds.barrowstodoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity

//TODO double check documentation on best practices here
@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_items")
    fun getTodoItems(): List<TodoItemEntity>

    @Query("SELECT * FROM todo_items WHERE id is :itemId")
    fun getTodoItemById(itemId: Int): TodoItemEntity

    @Update
    suspend fun updateTodoItem(todoItemEntity: TodoItemEntity)

    @Insert
    fun insertTodoItem(item: TodoItemEntity)

    @Query("DELETE FROM todo_items WHERE id = :itemId")
    suspend fun deleteTodoItemById(itemId: Int)
}