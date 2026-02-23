package web.athma.sadhane.feature.todoApp.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import web.athma.sadhane.core.ui.components.DialogState
import web.athma.sadhane.feature.todoApp.data.Todo
import web.athma.sadhane.feature.todoApp.data.TodoRepository

/**
 * ViewModel for the Todo feature. Holds UI state and exposes actions for [TodoScreen].
 *
 * Communicates with [TodoRepository] for all data operations and uses [viewModelScope]
 * to launch coroutines that are automatically cancelled when the ViewModel is cleared.
 *
 * @param repository The data source for todo items.
 */
class TodoViewModel(
    private val repository: TodoRepository
): ViewModel() {

    private val _todoTextState = mutableStateOf("")

    /**
     * The current text value of the new-task input field.
     * Bound to the TextField in [TodoScreen].
     */
    val todoTextState: State<String> = _todoTextState

    /**
     * A [Flow] of all todo items from the database.
     * Collecting this Flow automatically updates the UI when data changes.
     */
    val todos: Flow<List<Todo>> = repository.getTodos()

    /**
     * Updates the text in the new-task input field.
     *
     * @param todoText The new input value.
     */
    fun updateText(todoText: String) {
        _todoTextState.value = todoText
    }

    /**
     * Inserts a new todo using the current value of [todoTextState].
     * Should only be called when [todoTextState] is non-empty.
     */
    fun addTodo() {
        viewModelScope.launch {
            repository.addTodo(_todoTextState.value)
        }
    }

    /**
     * Persists a change to an existing todo (e.g. toggling its completion state).
     *
     * @param todo The todo with updated fields.
     */
    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    /**
     * Deletes all todo items from the database permanently.
     */
    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    /**
     * Deletes a specific todo item.
     *
     * @param todo The todo item to delete.
     */
    fun delete(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
