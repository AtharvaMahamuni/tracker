package web.athma.sadhane.feature.todoApp.ui

import androidx.lifecycle.ViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import web.athma.sadhane.feature.todoApp.data.TodoRepository

class TodoViewModelFactoryTest {

    private lateinit var repository: TodoRepository
    private lateinit var factory: TodoViewModelFactory

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        every { repository.getTodos() } returns flowOf(emptyList())
        factory = TodoViewModelFactory(repository)
    }

    @Test
    fun `create returns a TodoViewModel for TodoViewModel class`() {
        val viewModel = factory.create(TodoViewModel::class.java)

        assertNotNull(viewModel)
        assertTrue(viewModel is TodoViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create throws IllegalArgumentException for an unknown ViewModel class`() {
        factory.create(UnknownViewModel::class.java)
    }

    private class UnknownViewModel : ViewModel()
}
