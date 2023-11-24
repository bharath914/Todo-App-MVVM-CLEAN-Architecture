package com.bharath.todo_listapp.domain.usecases

import com.bharath.todo_listapp.domain.entity.TodoEntity
import com.bharath.todo_listapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTodoUseCase @Inject constructor(
    private val repository: Repository,
) {
    operator fun invoke(id: Int): Flow<Flow<TodoEntity>> = flow {

        val res = repository.getTodoById(id)
        emit(res)


    }

}