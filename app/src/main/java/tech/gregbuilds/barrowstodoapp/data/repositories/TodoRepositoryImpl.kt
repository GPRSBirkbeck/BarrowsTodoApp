package tech.gregbuilds.barrowstodoapp.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.gregbuilds.barrowstodoapp.data.dao.TodoDao
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import tech.gregbuilds.barrowstodoapp.util.TestData
import tech.gregbuilds.barrowstodoapp.util.toTodoItemUi
import javax.inject.Inject
import kotlin.text.equals
import kotlin.text.split

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {

    override suspend fun getTodoItems(): List<TodoItem> {
        return withContext(Dispatchers.IO) {
            todoDao.getTodoItems().map { entity ->
                entity.toTodoItemUi()
            }
        }
    }

    // As is best practice - the repository is responsible for mapping the entity to the UI model.
    override suspend fun getTodoItemById(id: Int): TodoItem {
        return withContext(Dispatchers.IO) {
            todoDao.getTodoItemById(id).toTodoItemUi()
        }
    }

    override suspend fun updateTodoItem(todoItemEntity: TodoItemEntity) {
        withContext(Dispatchers.IO) {
            todoDao.updateTodoItem(todoItemEntity)
        }
    }

    override suspend fun insertTodoItem(todoItemEntity: TodoItemEntity) {
        withContext(Dispatchers.IO) {
            todoDao.insertTodoItem(todoItemEntity)
        }
    }

    override suspend fun deleteTodoItem(id: Int) {
        withContext(Dispatchers.IO) {
            todoDao.deleteTodoItemById(id)
        }
    }

    override suspend fun addTestData() {
        withContext(Dispatchers.IO) {
            todoDao.insertAll(TestData.getTodoItems())
        }
    }

    override suspend fun getTodoItemsOrderedByWordFrequency(
        word: String,
        ascending: Boolean
    ): List<TodoItem> {
        return withContext(Dispatchers.IO) {
            val items = todoDao.getTodoItems().map { it.toTodoItemUi() }
            if (ascending) {
                items.sortedBy { item ->
                    val titleFrequency =
                        item.title.split(Regex("\\s+")).count { it.equals(word, ignoreCase = true) }
                    val bodyFrequency =
                        item.body.split(Regex("\\s+")).count { it.equals(word, ignoreCase = true) }
                    titleFrequency + bodyFrequency
                }
            } else {
                items.sortedByDescending { item ->
                    val titleFrequency =
                        item.title.split(Regex("\\s+")).count { it.equals(word, ignoreCase = true) }
                    val bodyFrequency =
                        item.body.split(Regex("\\s+")).count { it.equals(word, ignoreCase = true) }
                    titleFrequency + bodyFrequency
                }
            }
        }
    }

    override suspend fun searchTodoItems(searchQuery: String): List<TodoItem> {
        return withContext(Dispatchers.IO) {
            todoDao.searchTodoItems(searchQuery).map { it.toTodoItemUi() }
        }
    }
}