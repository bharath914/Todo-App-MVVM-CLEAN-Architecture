package com.bharath.todo_listapp.domain.usecases

import com.bharath.todo_listapp.data.other.Resource
import com.bharath.todo_listapp.domain.entity.TodoEntity
import com.bharath.todo_listapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CreateUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend operator fun invoke(todoEntity: TodoEntity) {
        try {
            repository.InsertTodo(todoEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

class UpdateUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend operator fun invoke(todoEntity: TodoEntity) {
        try {
            repository.update(todoEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

class DeleteUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend operator fun invoke(todoEntity: TodoEntity) {
        try {
            repository.delete(todoEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}


class GetAllTodosUseCase @Inject constructor(
    private val repository: Repository,
) {

    operator fun invoke(): Flow<Resource<Flow<List<TodoEntity>>>> = flow {

        try {
            emit(Resource.Loading())
            val res = repository.getAllTodos()
            emit(Resource.Success(data = res))


        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage ?: "Un expected"))
        }


    }


}