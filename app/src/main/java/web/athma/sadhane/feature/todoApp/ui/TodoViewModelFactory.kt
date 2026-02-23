package web.athma.sadhane.feature.todoApp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import web.athma.sadhane.feature.todoApp.data.TodoRepository

/**
 * Factory for creating [TodoViewModel] instances with a [TodoRepository] dependency.
 *
 * Required because [TodoViewModel] takes a constructor parameter. Pass this factory
 * to `viewModel(factory = ...)` in the composable to get a properly initialised instance.
 *
 * @param repository The repository to inject into [TodoViewModel].
 */
class TodoViewModelFactory(
    private val repository: TodoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
