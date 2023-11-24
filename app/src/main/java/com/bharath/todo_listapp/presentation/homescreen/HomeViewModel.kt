package com.bharath.todo_listapp.presentation.homescreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharath.todo_listapp.data.other.Resource
import com.bharath.todo_listapp.domain.entity.InvalidNoteException
import com.bharath.todo_listapp.domain.entity.TodoEntity
import com.bharath.todo_listapp.domain.usecases.CreateUseCase
import com.bharath.todo_listapp.domain.usecases.DeleteUseCase
import com.bharath.todo_listapp.domain.usecases.GetAllTodosUseCase
import com.bharath.todo_listapp.domain.usecases.UpdateUseCase
import com.bharath.todo_listapp.presentation.homescreen.events.HomeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

    private val getAllTodosUseCase: GetAllTodosUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val createUseCase: CreateUseCase,
    private val updateUseCase: UpdateUseCase,

    ) : ViewModel() {


    private val _todoItems = MutableStateFlow(emptyList<TodoEntity>())
    val todoItems = _todoItems.asStateFlow()

    private var getNotesJob: Job? = null
    private var recentlyDeletedNote: TodoEntity? = null

    private val _showSnackBar = MutableStateFlow(SnackBarData())
    val showSnackBar = _showSnackBar.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun resetSearchText(){
        _searchText.update { "" }
    }


    fun setShowSnackBarData(snackBarData: SnackBarData) {
        _showSnackBar.tryEmit(snackBarData)
    }

    fun onEvents(events: HomeEvents) {

        when (events) {
            is HomeEvents.DeleteNote -> {
                recentlyDeletedNote = events.todoEntity
                deleteTodo(events.todoEntity)
                _showSnackBar.tryEmit(
                    SnackBarData(
                        message = "Note Deleted",
                        show = true,
                        label = "undo"
                    )
                )
            }

            is HomeEvents.onClickOnNoteEvent -> {

            }

            is HomeEvents.RestoreNote -> {
                viewModelScope.launch(IO) {

                    restoreNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
                _showSnackBar.tryEmit(
                    SnackBarData(message = "Restored", show = true)
                )

            }

            is HomeEvents.UpdateNote -> {
                viewModelScope.launch(IO) {
                    try {

                        createUseCase(events.todoEntity)

                    } catch (e: InvalidNoteException) {
                        _showSnackBar.tryEmit(
                            SnackBarData(e.localizedMessage ?: "Unexpected Error", show = true)
                        )
                    }
                }
            }

            is HomeEvents.onSearchTextEntered -> {
                _searchText.tryEmit(events.text)


            }
        }
    }


    fun getAllTodos() {
        getNotesJob?.cancel()
        getNotesJob = getAllTodosUseCase().onEach { result ->

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


    private fun deleteTodo(entity: TodoEntity) {
        viewModelScope.launch(IO) {
            deleteUseCase(entity)
        }
    }

    private suspend fun restoreNote(entity: TodoEntity) {

        createUseCase(entity)

    }

    val filterItems: StateFlow<List<TodoEntity>> = searchText
        .onEach { }
        .combine(_todoItems) { text: String, list: List<TodoEntity> ->
            if (text.trim() == "") {
                emptyList()
            } else {
                list.filter {
                    it.searchQuery(text)
                }
            }
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

}


data class SnackBarData(
    val message: String = "",
    val show: Boolean = false,
    val label: String? = null,
)