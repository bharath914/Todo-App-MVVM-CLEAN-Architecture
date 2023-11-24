package com.bharath.todo_listapp.di


import android.app.Application
import androidx.room.Room
import com.bharath.todo_listapp.data.database.DbCons
import com.bharath.todo_listapp.data.database.Repositoryimpl
import com.bharath.todo_listapp.data.database.TodoDatabase
import com.bharath.todo_listapp.domain.repository.Repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Provides
    @Singleton
    fun provideDatabase(application: Application): TodoDatabase {
        return Room.databaseBuilder(
            application,
            TodoDatabase::class.java,
            name = DbCons.databaseName
        ).build()
    }


    @Provides
    @Singleton
    fun provideRepository(todoDatabase: TodoDatabase): Repository = Repositoryimpl(
        todoDatabase.dao
    )

}