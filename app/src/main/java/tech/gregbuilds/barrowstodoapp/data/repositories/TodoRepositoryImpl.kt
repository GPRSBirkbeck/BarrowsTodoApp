package tech.gregbuilds.barrowstodoapp.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.gregbuilds.barrowstodoapp.data.dao.TodoDao
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import tech.gregbuilds.barrowstodoapp.util.toTodoItemUi
import javax.inject.Inject

/*
 Hey reader, I'm keen to get your take on the approach of injecting the DateFormatterService into the repository
 and then passing it to the toTodoItemUi extension function.
 The other option was to inject the DateFormatterService into a mapping class, but that felt like overkill, and was not making use of extension functions, which are central to kotlin.
*/

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
}