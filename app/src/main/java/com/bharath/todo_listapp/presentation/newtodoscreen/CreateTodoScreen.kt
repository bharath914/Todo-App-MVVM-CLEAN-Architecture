package com.bharath.todo_listapp.presentation.newtodoscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bharath.todo_listapp.domain.entity.TodoEntity
import ir.kaaveh.sdpcompose.sdp
import com.bharath.todo_listapp.presentation.newtodoscreen.events.Create_Edit_Events as Event

@Composable
fun CreateTodoScreen(
    navHostController: NavHostController,
    padd: PaddingValues,
) {
    val viewModel = hiltViewModel<CrudViewModel>()

    LaunchedEffect(key1 = true, block = {
        viewModel.getNote()
    })
    CreateTodoContent(
        navHostController = navHostController,
        viewModel = viewModel,
        padd
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTodoContent(
    navHostController: NavHostController,
    viewModel: CrudViewModel,
    padd: PaddingValues,

    ) {



    val note = viewModel.todo.collectAsStateWithLifecycle()


    var title = viewModel.title.collectAsStateWithLifecycle()

    var description = viewModel.description.collectAsStateWithLifecycle()



    Scaffold(
        modifier = Modifier.padding(padd),
        topBar = {
            EditScreenTopBar(onclickOnSave = {
                viewModel.onEvent(
                    Event.OnSaveOnNote(
                        TodoEntity(
                            title = title.value,
                            description = description.value,
//                            isCompleted = note.value.isCompleted
                        )
                    )
                )

                navHostController.navigateUp()

            }, {
                viewModel.onEvent(
                    Event.OnBackButtonPressed(
                        TodoEntity(
                            title = title.value,
                            description = description.value,
//                            isCompleted = note.value.isCompleted
                        )
                    )
                )
                navHostController.navigateUp()
            }
            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {


            OutlinedTextField(
                value = title.value, onValueChange = {
                    viewModel.onEvent(Event.OnTitleEntered(it))
                }, modifier = Modifier
                    .padding(horizontal = 10.sdp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                ),
                placeholder = {
                    Text(text = "Enter Name or Title")
                }

            )
            Spacer(modifier = Modifier.height(15.sdp))

            OutlinedTextField(
                value = description.value, onValueChange = {
                    viewModel.onEvent(Event.OnDescEntered(it))
                },

                modifier = Modifier
                    .padding(10.sdp)
                    .fillMaxSize(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                ),
                placeholder = {
                    Text(text = "Start writing something ........")
                }
            )


        }
    }


}

@Composable
private fun EditScreenTopBar(
    onclickOnSave: () -> Unit,
    onClickBackButton: () -> Unit,

    ) {

    Row(
        modifier = Modifier
            .height(50.sdp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(onClick = { onClickBackButton() }) {
            Icon(imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = null)
        }
        IconButton(onClick = { onclickOnSave() }) {
            Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
        }


    }

}