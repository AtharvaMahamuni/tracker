package web.athma.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import web.athma.tracker.core.data.database.TrackerDatabase
import web.athma.tracker.core.ui.components.AppAlertDialog
import web.athma.tracker.core.ui.components.CoreViewModel
import web.athma.tracker.feature.todoApp.data.TodoRepository
import web.athma.tracker.feature.todoApp.ui.TodoScreen
import web.athma.tracker.feature.todoApp.ui.TodoViewModelFactory
import web.athma.tracker.core.ui.theme.TrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the database
        val database = TrackerDatabase.getInstance(applicationContext)

        // Get the DAO
        val dao = database.todoDao()

        // Create the Repository
        val repository = TodoRepository(dao)

        // Create a factory for ViewModel
        val viewModelFactory = TodoViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            val coreViewModel: CoreViewModel = viewModel()
            AppAlertDialog(state = coreViewModel.dialogState)


            TrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoScreen(
                        modifier = Modifier.padding(innerPadding),
                        todoViewModelFactory = viewModelFactory,
                        coreViewModel = coreViewModel
                    )
                }
            }
        }
    }
}
