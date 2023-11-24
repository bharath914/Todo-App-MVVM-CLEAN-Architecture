package com.bharath.todo_listapp.data.database

import com.bharath.todo_listapp.domain.entity.TodoEntity
import com.bharath.todo_listapp.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class Repositoryimpl @Inject constructor(
    private val dao: TodoDao,
) : Repository {
    override suspend fun InsertTodo(todoEntity: TodoEntity) {
        withContext(Dispatchers.IO) {
            dao.InsertTodo(todoEntity)
        }
    }

    override suspend fun delete(todoEntity: TodoEntity) {
        withContext(Dispatchers.IO) {
            dao.delete(todoEntity)
        }
    }

    override suspend fun update(todoEntity: TodoEntity) {
        withContext(Dispatchers.IO) {
            dao.update(todoEntity)
        }
    }

    override suspend fun getAllTodos(): Flow<List<TodoEntity>> {
        return withContext(Dispatchers.IO) {
            dao.getAllTodos()
        }
    }

    override suspend fun getTodoById(id: Int): Flow<TodoEntity> {
        return withContext(IO) {
            dao.getTodoById(id)
        }
    }


}