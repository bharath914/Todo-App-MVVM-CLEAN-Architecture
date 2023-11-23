package com.bharath.todo_listapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bharath.todo_listapp.data.entity.TodoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {


    @Insert(entity = TodoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertTodo(todoEntity: TodoEntity)


    @Delete
    suspend fun delete(todoEntity: TodoEntity)

    @Update(entity = TodoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(todoEntity: TodoEntity)


    @Query(
        "select * from TodoApp "
    )
    fun getAllTodos(): Flow<List<TodoEntity>>

}