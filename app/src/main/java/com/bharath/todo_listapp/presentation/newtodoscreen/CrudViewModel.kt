package com.bharath.todo_listapp.presentation.newtodoscreen


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharath.todo_listapp.domain.entity.TodoEntity
import com.bharath.todo_listapp.domain.usecases.CreateUseCase
import com.bharath.todo_listapp.domain.usecases.DeleteUseCase
import com.bharath.todo_listapp.domain.usecases.GetTodoUseCase
import com.bharath.todo_listapp.domain.usecases.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import com.bharath.todo_listapp.presentation.newtodoscreen.events.Create_Edit_Events as Event

@HiltViewModel
class CrudViewModel @Inject constructor(
    private val createUseCase: CreateUseCase,
    private val updateUseCase: UpdateUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getTodoUseCase: GetTodoUseCase,


    ) : ViewModel() {


    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()


    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _todo = MutableStateFlow(TodoEntity())
    val todo = _todo.asStateFlow()

    private var deletedNote: TodoEntity? = null
    private var isNewNote: Boolean = false


    private fun getDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }

    private fun getTime(): String {
        val time = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(time.time)
    }

    init {
        isNewNote = getNote()
    }

    private fun getNote(): Boolean {
        savedStateHandle.get<String>("Id")?.let {
            if (it.isNotBlank() && it.isNotEmpty()) {
                Log.d("Crud", "getNote:$it ")
                getNoteWithId(it.toInt())
                return false
            }
        }
        return true
    }

    fun onEvent(event: Event) {

        when (event) {
            is Event.OnSaveOnNote -> {

                if (!isNewNote) {
                    updateFun()
                } else {
                    insertFun()
                }

            }

            is Event.OnBackButtonPressed -> {
                if (!isNewNote) {
                    updateFun()
                } else {

                    insertFun()
                }
            }

            is Event.OnTitleEntered -> {
                _title.tryEmit(event.title)
            }

            is Event.OnDescEntered -> {
                _description.tryEmit(event.desc)
            }

            is Event.DeleteNote -> {
                deletedNote = event.note
                delete(event.note)
                _title.tryEmit("")
                _description.tryEmit("")

            }

            is Event.RestoreNote -> {
                viewModelScope.launch(IO) {
                    insertIntoDb(deletedNote ?: return@launch)
                }
            }

        }

    }


    private fun insertIntoDb(entity: TodoEntity) {

        viewModelScope.launch(IO) {

            createUseCase(entity)
        }


    }

    private fun insertFun() {
        if (_title.value.isNotEmpty()) {
            insertIntoDb(
                TodoEntity(
                    title = _title.value,
                    description = _description.value,
                    time = getTime(),
                    date = getDate()
                )
            )
        }else{

        }
    }

    private fun updateFun() {
        val note = TodoEntity(
            id = _todo.value.id,
            title = _title.value,
            description = _description.value,
            isCompleted = _todo.value.isCompleted,
            time = getTime(),
            date = getDate()
        )
        update(note)
    }

    private fun update(entity: TodoEntity) {
        viewModelScope.launch(IO) {
            updateUseCase(entity)
        }
    }

    private fun delete(entity: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUseCase(entity)
        }
    }

    private fun getNoteWithId(id: Int) {

        getTodoUseCase(id).onEach {
            it.collect { entity ->


                _todo.tryEmit(entity)
                _title.tryEmit(entity.title)
                _description.tryEmit(entity.description)
            }
        }.launchIn(viewModelScope)
    }

}