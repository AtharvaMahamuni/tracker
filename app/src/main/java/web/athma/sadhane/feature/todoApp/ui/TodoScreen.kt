package web.athma.sadhane.feature.todoApp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AddCircle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import web.athma.sadhane.core.ui.components.CoreViewModel
import kotlin.collections.emptyList
import kotlin.collections.reversed

/**
 * The main screen for the Todo feature.
 *
 * Displays a list of todo items with checkboxes and delete buttons, a top bar with a
 * "Clear All" action, and a bottom bar with a text field and add button for creating new tasks.
 *
 * Todo items are shown in reverse insertion order (newest at the top).
 * Completed items are rendered with strikethrough text and a gray color.
 *
 * Destructive actions (delete, clear all) route through [CoreViewModel.showConfirmation]
 * so they display a shared confirmation dialog before executing.
 *
 * @param modifier Modifier applied to the root [Scaffold].
 * @param todoViewModelFactory Factory used to create [TodoViewModel] with its repository.
 * @param coreViewModel Shared ViewModel for triggering app-wide confirmation dialogs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    todoViewModelFactory: TodoViewModelFactory,
    coreViewModel: CoreViewModel
) {
    val viewModel: TodoViewModel = viewModel(factory = todoViewModelFactory)
    val todosState = viewModel.todos.collectAsState(initial = emptyList())

    val todoTextState = viewModel.todoTextState
    val todos = todosState.value.reversed()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sadhane") },
                actions = {
                    TextButton(onClick = {
                        if (todos.isNotEmpty()) {
                            coreViewModel.showConfirmation(
                                title = "Delete all Tasks?",
                                message = "Do you really want to delete all tasks? Once done can't be undone!",
                                onConfirm = {
                                    viewModel.clearAll()
                                }
                            )
                        }
                    }) {
                        Text("Clear All")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .weight(4f),
                    value = todoTextState.value,
                    onValueChange = { viewModel.updateText(it) },
                )
                IconButton(
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
        LazyColumn(
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
                        modifier = Modifier.weight(1f),
                        text = todo.task,
                        overflow = TextOverflow.Ellipsis,
                        // Strikethrough and gray color indicate a completed task
                        textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                        color = if (todo.isDone) Color.Gray else Color.Unspecified
                    )
                    IconButton(onClick = {
                        coreViewModel.showConfirmation(
                            title = "Delete Task",
                            message = "Do you want to delete this ${todo.task}?",
                            onConfirm = {
                                viewModel.delete(todo)
                            }
                        )
                    }) {
                        Icon(
                            modifier = Modifier.padding(end = 5.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Add task"
                        )
                    }
                }
            }
        }
    }
}
