package web.athma.toytools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import web.athma.toytools.core.data.database.ToyToolsDatabase
import web.athma.toytools.core.ui.components.AppAlertDialog
import web.athma.toytools.core.ui.components.CoreViewModel
import web.athma.toytools.feature.todoApp.data.TodoRepository
import web.athma.toytools.feature.todoApp.ui.TodoScreen
import web.athma.toytools.feature.todoApp.ui.TodoViewModelFactory
import web.athma.toytools.core.ui.theme.ToyToolsTheme

/**
 * The single Activity that hosts the entire Toy Tools app.
 *
 * Responsible for:
 * - Bootstrapping the dependency graph (database → DAO → repository → ViewModel factory)
 * - Providing the [CoreViewModel] shared across all feature screens
 * - Rendering the [AppAlertDialog] overlay at the top level so it can appear over any screen
 * - Applying the [ToyToolsTheme] and routing to the appropriate feature screen
 *
 * As new utility features are added, wire their dependencies here and add their screens
 * to the navigation/content area inside [setContent].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bootstrap the Todo feature dependencies
        val database = ToyToolsDatabase.getInstance(applicationContext)
        val dao = database.todoDao()
        val repository = TodoRepository(dao)
        val viewModelFactory = TodoViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            // CoreViewModel is scoped to the Activity and shared with all feature screens
            val coreViewModel: CoreViewModel = viewModel()

            // AppAlertDialog sits above all screens to render confirmation dialogs app-wide
            AppAlertDialog(state = coreViewModel.dialogState)

            ToyToolsTheme {
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
