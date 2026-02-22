package web.athma.tracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.count
import kotlin.collections.emptyList
import kotlin.collections.reversed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(modifier: Modifier = Modifier, todoViewModelFactory: TodoViewModelFactory) {
    val viewModel: TodoViewModel = viewModel(factory = todoViewModelFactory)
    val todosState = viewModel.todos.collectAsState(initial = emptyList())

    val todoTextState = viewModel.todoTextState
    val todos = todosState.value.reversed()

    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tracker App") },
                actions = {
                    TextButton(onClick = {
                        showDialog.value = true
                    }) {
                        Text("Clear All")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically  // Add this
            ) {
                TextField(
                    modifier = Modifier
                        .weight(4f),
                    value = todoTextState.value,
                    onValueChange = { viewModel.updateText(it) },
                )
                IconButton (
                    onClick = {
                        if (todoTextState.value.isNotEmpty()) {
                            viewModel.addTodo()
                            viewModel.updateText("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "Add task"
                    )
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->

        LazyColumn (
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues),
            reverseLayout = true
        ) {
            items(todos) { todo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = todo.isDone,
                        onCheckedChange = { isChecked ->
                            viewModel.updateTodo(
                                todo.copy(isDone = isChecked)
                            )
                        }
                    )
                    Text(
                        text = todo.task,
                        textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                        color = if (todo.isDone) Color.Gray else Color.Unspecified
                    )
                }
            }
        }

        // Add this dialog:
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false  // ← Close dialog when clicking outside
                },
                title = {
                    Text(text = "Clear All Todos?")
                },
                text = {
                    Text(text = "This will permanently delete all todos. This action cannot be undone.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearAll()      // ← Actually delete
                            showDialog.value = false  // ← Close dialog
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog.value = false  // ← Just close, don't delete
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
