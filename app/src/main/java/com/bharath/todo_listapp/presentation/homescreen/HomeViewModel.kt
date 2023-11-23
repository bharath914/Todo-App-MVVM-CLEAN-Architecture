package com.bharath.todo_listapp.presentation.homescreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharath.todo_listapp.data.entity.TodoEntity
import com.bharath.todo_listapp.data.other.Resource
import com.bharath.todo_listapp.usecases.DeleteUseCase
import com.bharath.todo_listapp.usecases.GetAllTodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

    private val getAllTodosUseCase: GetAllTodosUseCase,
    private val deleteUseCase: DeleteUseCase,

    ) : ViewModel() {


    private val _todoItems = MutableStateFlow(emptyList<TodoEntity>())
    val todoItems = _todoItems.asStateFlow()


    fun getAllTodos() {
        getAllTodosUseCase().onEach { result ->

            when (result) {
                is Resource.Success -> {
                    result.data?.collectLatest { list ->


                        _todoItems.tryEmit(
                            list
                        )
                    }

                }

                is Resource.Loading -> {
                    // not needed for local databases
                }

                else -> {
                    /// handling error messages
                }
            }

        }.launchIn(viewModelScope)
    }


    fun deleteTodo(entity: TodoEntity) {
        viewModelScope.launch(IO) {
            deleteUseCase(entity)
        }
    }

}