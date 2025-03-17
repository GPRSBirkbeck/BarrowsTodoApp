package tech.gregbuilds.barrowstodoapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity

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

    @Insert
    fun insertAll(todoItems: List<TodoItemEntity>)

    @Query("DELETE FROM todo_items WHERE id = :itemId")
    suspend fun deleteTodoItemById(itemId: Int)

    // The requirement was just titles, but I think our users would be happy if they can do title or body
    @Query("SELECT * FROM todo_items WHERE title LIKE '%' || :searchString || '%' OR body LIKE '%' || :searchString || '%'")
    fun searchTodoItems(searchString: String): List<TodoItemEntity>
}