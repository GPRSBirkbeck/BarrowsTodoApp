package tech.gregbuilds.barrowstodoapp.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.gregbuilds.barrowstodoapp.data.dao.TodoDao
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import tech.gregbuilds.barrowstodoapp.util.TestData
import tech.gregbuilds.barrowstodoapp.util.toTodoItemUi
import javax.inject.Inject

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
}