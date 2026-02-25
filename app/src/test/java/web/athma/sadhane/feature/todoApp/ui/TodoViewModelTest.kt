package web.athma.sadhane.feature.todoApp.ui

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import web.athma.sadhane.feature.todoApp.data.Todo
import web.athma.sadhane.feature.todoApp.data.TodoRepository

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: TodoRepository
    private lateinit var viewModel: TodoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getTodos() } returns flowOf(emptyList())
        viewModel = TodoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- todoTextState ---

    @Test
    fun `initial todoTextState is empty string`() {
        assertEquals("", viewModel.todoTextState.value)
    }

    @Test
    fun `updateText sets todoTextState to given value`() {
        viewModel.updateText("Buy groceries")

        assertEquals("Buy groceries", viewModel.todoTextState.value)
    }

    @Test
    fun `updateText with empty string clears todoTextState`() {
        viewModel.updateText("some text")
        viewModel.updateText("")

        assertEquals("", viewModel.todoTextState.value)
    }

    @Test
    fun `updateText called multiple times keeps last value`() {
        viewModel.updateText("first")
        viewModel.updateText("second")
        viewModel.updateText("third")

        assertEquals("third", viewModel.todoTextState.value)
    }

    // --- todos flow ---

    @Test
    fun `todos emits list from repository`() = runTest {
        val todos = listOf(
            Todo(id = 1, task = "Task 1", isDone = false),
            Todo(id = 2, task = "Task 2", isDone = true)
        )
        every { repository.getTodos() } returns flowOf(todos)
        val vm = TodoViewModel(repository)

        val result = vm.todos.first()

        assertEquals(todos, result)
    }

    // --- addTodo ---

    @Test
    fun `addTodo calls repository with current todoTextState`() = runTest {
        viewModel.updateText("Write tests")

        viewModel.addTodo()

        coVerify { repository.addTodo("Write tests") }
    }

    @Test
    fun `addTodo calls repository with empty string when text not set`() = runTest {
        viewModel.addTodo()

        coVerify { repository.addTodo("") }
    }

    // --- updateTodo ---

    @Test
    fun `updateTodo calls repository with given todo`() = runTest {
        val todo = Todo(id = 1, task = "Buy groceries", isDone = true)

        viewModel.updateTodo(todo)

        coVerify { repository.updateTodo(todo) }
    }

    // --- clearAll ---

    @Test
    fun `clearAll calls repository deleteAll`() = runTest {
        viewModel.clearAll()

        coVerify { repository.deleteAll() }
    }

    // --- delete ---

    @Test
    fun `delete calls repository delete with given todo`() = runTest {
        val todo = Todo(id = 2, task = "Read a book", isDone = false)

        viewModel.delete(todo)

        coVerify { repository.delete(todo) }
    }
}
