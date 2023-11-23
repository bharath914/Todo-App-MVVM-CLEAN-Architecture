package com.bharath.todo_listapp.presentation.newtodoscreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharath.todo_listapp.data.entity.TodoEntity
import com.bharath.todo_listapp.usecases.CreateUseCase
import com.bharath.todo_listapp.usecases.DeleteUseCase
import com.bharath.todo_listapp.usecases.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudViewModel @Inject constructor(
    private val createUseCase: CreateUseCase,
    private val updateUseCase: UpdateUseCase,
    private val deleteUseCase: DeleteUseCase,

    ) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()


    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()


    fun insertIntoDb(entity: TodoEntity) {
        viewModelScope.launch(IO) {
            createUseCase(entity)
        }

    }

    fun update(entity: TodoEntity) {
        viewModelScope.launch(IO) {
            updateUseCase(entity)
        }
    }

    fun delete(entity: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUseCase(entity)
        }
    }

}