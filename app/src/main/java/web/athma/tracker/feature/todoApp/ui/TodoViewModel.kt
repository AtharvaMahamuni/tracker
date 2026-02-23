package web.athma.tracker.feature.todoApp.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import web.athma.tracker.core.ui.components.DialogState
import web.athma.tracker.feature.todoApp.data.Todo
import web.athma.tracker.feature.todoApp.data.TodoRepository

class TodoViewModel(
    private val repository: TodoRepository
): ViewModel() {
    private val _todoTextState =  mutableStateOf("")
    val todoTextState: State<String> = _todoTextState

    val todos: Flow<List<Todo>> = repository.getTodos()

    fun updateText(todoText: String) {
        _todoTextState.value = todoText
    }

    fun addTodo() {
        viewModelScope.launch {
            repository.addTodo(_todoTextState.value)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
