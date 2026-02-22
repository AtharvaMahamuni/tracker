package web.athma.todo.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    fun getTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }

    suspend fun addTodo(task: String) {
        todoDao.insert(
            Todo(
                id = 0,
                task = task,
                isDone = false
            )
        )
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo = todo)
    }
}