package com.bharath.todo_listapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bharath.todo_listapp.data.database.DbCons
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity(
    tableName = "TodoApp"
)
@Serializable
data class TodoEntity(
    @SerialName(DbCons.id)
    @PrimaryKey @ColumnInfo(name = DbCons.id) val id: Int = 0,


    @SerialName(DbCons.title)
    @ColumnInfo(name = DbCons.title) val title: String = "",


    @SerialName(DbCons.desc)
    @ColumnInfo(name = DbCons.desc) val description: String = "",


    @SerialName(DbCons.isCompleted)
    @ColumnInfo(name = DbCons.isCompleted) val isCompleted: Boolean = false,


    @SerialName(DbCons.time)
    @ColumnInfo(name = DbCons.time) val time: String = "",

    @SerialName(DbCons.date)
    @ColumnInfo(name = DbCons.date) val date: String = "",
)