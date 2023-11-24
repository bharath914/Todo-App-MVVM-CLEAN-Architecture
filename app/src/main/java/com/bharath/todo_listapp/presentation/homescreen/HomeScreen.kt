package com.bharath.todo_listapp.presentation.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bharath.todo_listapp.domain.entity.TodoEntity
import com.bharath.todo_listapp.presentation.homescreen.components.NotesCard
import com.bharath.todo_listapp.presentation.homescreen.events.HomeEvents
import com.bharath.todo_listapp.presentation.navigation.NavConst
import ir.kaaveh.sdpcompose.sdp


@Composable
fun HomeScreen(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    val viewmodel = hiltViewModel<HomeViewModel>()

    LaunchedEffect(key1 = true, block = {
        viewmodel.getAllTodos()


    })
    HomeContent(viewModel = viewmodel, navHostController = navHostController)


}

@Composable
private fun HomeContent(
    viewModel: HomeViewModel,
    navHostController: NavHostController,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    val snackBarData = viewModel.showSnackBar.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = snackBarData.value.show, block = {
        if (snackBarData.value.show) {
            val res = snackBarHostState.showSnackbar(
                snackBarData.value.message, actionLabel = snackBarData.value.label,

                duration = SnackbarDuration.Short
            )
            if (res == SnackbarResult.ActionPerformed) {
                viewModel.onEvents(HomeEvents.RestoreNote)
                viewModel.setShowSnackBarData(SnackBarData())
            } else if (res == SnackbarResult.Dismissed) {
                viewModel.setShowSnackBarData(SnackBarData())
            }
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(NavConst.editScreen)
            }, modifier = Modifier.padding(10.sdp)) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerpad ->
        Column(
            modifier = Modifier.padding(innerpad),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            /*
            Home Screen Contents
             */
            val cardmod = Modifier
                .padding(10.sdp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
            val listOfTodos = viewModel.todoItems.collectAsStateWithLifecycle()


            Search(viewModel = viewModel, modifier = cardmod, navHostController = navHostController)
            Spacer(modifier = Modifier.height(15.sdp))
            LazyColumn(content = {

                items(listOfTodos.value) {
                    NotesCard(modifier = cardmod, note = it, onClickDelete = {
                        viewModel.onEvents(HomeEvents.DeleteNote(it))
                    }, onClickCard = {
                        viewModel.onEvents(HomeEvents.onClickOnNoteEvent(it))
                        navHostController.navigate(NavConst.editScreen + "/${it.id}")
                    }) {

                        viewModel.onEvents(
                            HomeEvents.UpdateNote(
                                TodoEntity(
                                    id = it.id,
                                    title = it.title,
                                    description = it.description,
                                    isCompleted = !it.isCompleted,
                                    time = it.time,
                                    date = it.date
                                )
                            )
                        )

                    }
                }
            })
        }
    }


}


suspend fun showSnackBar(
    snackbarHostState: SnackbarHostState,

    viewmodel: HomeViewModel,
) {


    val res = snackbarHostState.showSnackbar(
        message = "Note deleted", actionLabel = "Undo", duration = SnackbarDuration.Long

    )
    when (res) {
        SnackbarResult.ActionPerformed -> {
            viewmodel.onEvents(HomeEvents.RestoreNote)
        }

        else -> {

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Search(
    viewModel: HomeViewModel,
    modifier: Modifier,
    navHostController: NavHostController,
) {
    val searchText = viewModel.searchText.collectAsStateWithLifecycle()
    var isSearchActive by remember {
        mutableStateOf(false)
    }



    SearchBar(query = searchText.value, onQueryChange = {
        viewModel.onEvents(HomeEvents.onSearchTextEntered(it))
    }, onSearch = {

    }, active = isSearchActive, onActiveChange = {
        isSearchActive = !isSearchActive
    },


        placeholder = {
            Text(text = "Search for notes..")
        }, leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
        }, trailingIcon = {
            if (isSearchActive) {
                IconButton(onClick = {
                    isSearchActive = !isSearchActive
                    viewModel.resetSearchText()
                }) {

                    Icon(imageVector = Icons.Rounded.Clear, contentDescription = null)
                }
            }
        }

    ) {
        val filteredItems = viewModel.filterItems.collectAsStateWithLifecycle()
        LazyColumn(content = {
            items(filteredItems.value) {
                NotesCard(modifier = modifier,
                    note = it,
                    onClickDelete = { viewModel.onEvents(HomeEvents.DeleteNote(it)) },
                    onClickCard = {
                        viewModel.onEvents(HomeEvents.onClickOnNoteEvent(it))
                        navHostController.navigate(NavConst.editScreen + "/${it.id}")
                    }) {
                    viewModel.onEvents(
                        HomeEvents.UpdateNote(
                            TodoEntity(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                isCompleted = !it.isCompleted,
                                time = it.time,
                                date = it.date
                            )
                        )
                    )
                }
            }
        })
    }


}