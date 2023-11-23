package com.bharath.todo_listapp.presentation.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    val viewmodel = hiltViewModel<HomeViewModel>()

    LaunchedEffect(key1 = true, block = {
        viewmodel.getAllTodos()
    })
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerpad ->
        Column(
            modifier = Modifier.padding(innerpad)
        ) {


        }
    }

}

@Composable
private fun HomeContent(

) {


}