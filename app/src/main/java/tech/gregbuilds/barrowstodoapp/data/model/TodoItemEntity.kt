package tech.gregbuilds.barrowstodoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "due_date") val dueDateLong: Long,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "due_date_string") val dueDateString: String,
    @ColumnInfo(name = "icon_identifier") val iconIdentifier: String
)