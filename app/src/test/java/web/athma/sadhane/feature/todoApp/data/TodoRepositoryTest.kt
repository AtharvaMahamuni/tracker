package web.athma.sadhane.feature.todoApp.data

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TodoRepositoryTest {

    private lateinit var dao: TodoDao
    private lateinit var repository: TodoRepository

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = TodoRepository(dao)
    }

    @Test
    fun `getTodos delegates to dao getAllTodos`() {
        every { dao.getAllTodos() } returns flowOf(emptyList())

        repository.getTodos()

        verify { dao.getAllTodos() }
    }

    @Test
    fun `getTodos returns flow emitted by dao`() = runTest {
        val todos = listOf(
            Todo(id = 1, task = "Task 1", isDone = false),
            Todo(id = 2, task = "Task 2", isDone = true)
        )
        every { dao.getAllTodos() } returns flowOf(todos)

        val result = repository.getTodos().first()

        assertEquals(todos, result)
    }

    @Test
    fun `addTodo inserts Todo with given task and isDone false`() = runTest {
        repository.addTodo("Buy groceries")

        coVerify { dao.insert(Todo(id = 0, task = "Buy groceries", isDone = false)) }
    }

    @Test
    fun `addTodo uses exactly the provided task text`() = runTest {
        repository.addTodo("  spaced task  ")

        coVerify { dao.insert(Todo(id = 0, task = "  spaced task  ", isDone = false)) }
    }

    @Test
    fun `updateTodo delegates to dao update with the given todo`() = runTest {
        val todo = Todo(id = 1, task = "Buy groceries", isDone = true)

        repository.updateTodo(todo)

        coVerify { dao.update(todo) }
    }

    @Test
    fun `deleteAll delegates to dao deleteAll`() = runTest {
        repository.deleteAll()

        coVerify { dao.deleteAll() }
    }

    @Test
    fun `delete delegates to dao delete with the given todo`() = runTest {
        val todo = Todo(id = 3, task = "Read a book", isDone = false)

        repository.delete(todo)

        coVerify { dao.delete(todo) }
    }
}
