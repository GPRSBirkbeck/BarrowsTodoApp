package tech.gregbuilds.barrowstodoapp.data.repositories

import tech.gregbuilds.barrowstodoapp.data.dao.TodoDao
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import tech.gregbuilds.barrowstodoapp.util.DateFormatterService
import tech.gregbuilds.barrowstodoapp.util.toTodoItemUi
import javax.inject.Inject

/*
 Hey reader, I'm keen to get your take on the approach of injecting the DateFormatterService into the repository
 and then passing it to the toTodoItemUi extension function.
 The other option was to inject the DateFormatterService into a mapping class, but that felt like overkill, and was not making use of extension functions, which are central to kotlin.
*/

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val dateFormatterService: DateFormatterService
) : TodoRepository {

     override suspend fun getTodoItems(): List<TodoItem> {
        return todoDao.getTodoItems().map { entity ->
            entity.toTodoItemUi(dateFormatterService)
        }
    }

     override suspend fun getTodoItemById(id: Int): TodoItem {
        return todoDao.getTodoItemById(id).toTodoItemUi(dateFormatterService)
    }

     override suspend fun updateTodoItem(todoItemEntity: TodoItemEntity) {
        todoDao.updateTodoItem(todoItemEntity)
    }

     override suspend fun insertTodoItem(todoItemEntity: TodoItemEntity) {
        todoDao.insertTodoItem(todoItemEntity)
    }

     override suspend fun deleteTodoItem(todoItemEntity: TodoItemEntity) {
        todoDao.deleteTodoItem(todoItemEntity)
    }
}