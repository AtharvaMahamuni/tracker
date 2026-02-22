package web.athma.todo.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import web.athma.todo.data.Todo
import web.athma.todo.data.TodoRepository

class TodoViewModel(
    private val repository: TodoRepository
): ViewModel() {
    private val _todoTextState =  mutableStateOf("")
    val todoTextState: State<String> = _todoTextState

    // Todo data
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
}