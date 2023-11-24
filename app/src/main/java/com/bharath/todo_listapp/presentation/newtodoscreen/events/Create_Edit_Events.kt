package com.bharath.todo_listapp.presentation.newtodoscreen.events

import com.bharath.todo_listapp.domain.entity.TodoEntity

sealed class Create_Edit_Events() {
    data class OnTitleEntered(val title: String) : Create_Edit_Events()
    data class OnDescEntered(val desc: String) : Create_Edit_Events()
    data class OnSaveOnNote(val note: TodoEntity) : Create_Edit_Events()
    data class OnBackButtonPressed(val note: TodoEntity) : Create_Edit_Events()
    data class DeleteNote(val note: TodoEntity) : Create_Edit_Events()
    object RestoreNote : Create_Edit_Events()

}
