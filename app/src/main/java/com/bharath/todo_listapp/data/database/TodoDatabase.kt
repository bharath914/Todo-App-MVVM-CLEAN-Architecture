package com.bharath.todo_listapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bharath.todo_listapp.domain.entity.TodoEntity


@Database(
    entities = [TodoEntity::class],
    version = 1
)
abstract class TodoDatabase : RoomDatabase() {
    abstract val dao: TodoDao
}

object DbCons {
    const val tableName = "TodoEntity"
    const val databaseName = "TodoDatabase"

    const val title = "Title"
    const val isCompleted = "isCompleted"
    const val id = "Id"
    const val desc = "Description"
    const val date = "Date"
    const val time = "Time"
}
