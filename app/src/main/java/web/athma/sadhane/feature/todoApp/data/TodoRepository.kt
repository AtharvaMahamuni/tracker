package web.athma.sadhane.feature.todoApp.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository for the Todo feature. Acts as the single source of truth for todo data.
 *
 * Abstracts the data source (Room database via [TodoDao]) from the ViewModel layer,
 * so the ViewModel never talks directly to the DAO.
 *
 * @param todoDao The DAO used to perform database operations.
 */
class TodoRepository(private val todoDao: TodoDao) {

    /**
     * Returns a [Flow] emitting the current list of all todos.
     * Automatically emits a new list whenever the underlying database changes.
     */
    fun getTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }

    /**
     * Inserts a new todo item with the given task text.
     * The new item is created with [isDone] set to `false`.
     *
     * @param task The text content of the new todo.
     */
    suspend fun addTodo(task: String) {
        todoDao.insert(
            Todo(
                id = 0,
                task = task,
                isDone = false
            )
        )
    }

    /**
     * Updates an existing todo item (e.g. toggling its [Todo.isDone] state).
     *
     * @param todo The todo item with the updated values.
     */
    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo = todo)
    }

    /**
     * Deletes all todo items from the database permanently.
     */
    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    /**
     * Deletes a specific todo item from the database.
     *
     * @param todo The todo item to delete.
     */
    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }
}
