package web.athma.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import web.athma.tracker.todoApp.data.TodoDatabase
import web.athma.tracker.todoApp.data.TodoRepository
import web.athma.tracker.todoApp.ui.TodoScreen
import web.athma.tracker.todoApp.ui.TodoViewModelFactory
import web.athma.tracker.ui.theme.TrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the database
        val database = TodoDatabase.getInstance(applicationContext)

        // Get the DAO
        val dao = database.todoDao()

        // Create the Repository
        val repository = TodoRepository(dao)

        // Create a factory for ViewModel
        val viewModelFactory = TodoViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            TrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoScreen(
                        modifier = Modifier.padding(innerPadding),
                        todoViewModelFactory = viewModelFactory
                    )
                }
            }
        }
    }
}
