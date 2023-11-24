package com.bharath.todo_listapp.domain.repository

import com.bharath.todo_listapp.domain.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun InsertTodo(todoEntity: TodoEntity)
    suspend fun delete(todoEntity: TodoEntity)
    suspend fun update(todoEntity: TodoEntity)
    suspend fun getAllTodos(): Flow<List<TodoEntity>>


    suspend fun getTodoById(id: Int): Flow<TodoEntity>
}
