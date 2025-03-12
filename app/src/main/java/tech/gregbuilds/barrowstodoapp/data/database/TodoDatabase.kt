package tech.gregbuilds.barrowstodoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.gregbuilds.barrowstodoapp.data.dao.TodoDao
import tech.gregbuilds.barrowstodoapp.data.model.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}