package com.bharath.todo_listapp.presentation.homescreen.events

import com.bharath.todo_listapp.domain.entity.TodoEntity

sealed class HomeEvents {
    data class onClickOnNoteEvent(val todoEntity: TodoEntity) : HomeEvents()
    data class DeleteNote(val todoEntity: TodoEntity) : HomeEvents()
    object RestoreNote : HomeEvents()

    data class UpdateNote(val todoEntity: TodoEntity):HomeEvents()

    data class onSearchTextEntered(val text:String):HomeEvents()

}

