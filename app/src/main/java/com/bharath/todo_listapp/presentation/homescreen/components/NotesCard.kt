package com.bharath.todo_listapp.presentation.homescreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.bharath.todo_listapp.domain.entity.TodoEntity
import ir.kaaveh.sdpcompose.ssp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesCard(
    modifier: Modifier,
    note: TodoEntity,
    onClickDelete: () -> Unit,
    onClickCard: () -> Unit,
    onRadioButtonClick: () -> Unit,
) {
    Card(onClick = { onClickCard() }, modifier = modifier.alpha(if (note.isCompleted) 0.4f else 1f)) {

        var isSelected by remember {
            mutableStateOf(note.isCompleted)
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                RadioButton(selected = isSelected, onClick = {
                    isSelected = !isSelected
                    onRadioButtonClick()

                })
            }
            Box(modifier = Modifier.weight(5f), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = note.title,
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                IconButton(onClick = {
                    onClickDelete()
                }) {
                    Icon(imageVector = Icons.Rounded.DeleteOutline, contentDescription = "")
                }
            }
        }
    }

}