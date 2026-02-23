package web.athma.sadhane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import web.athma.sadhane.core.data.database.SadhaneDatabase
import web.athma.sadhane.core.ui.components.AppAlertDialog
import web.athma.sadhane.core.ui.components.CoreViewModel
import web.athma.sadhane.feature.todoApp.data.TodoRepository
import web.athma.sadhane.feature.todoApp.ui.TodoScreen
import web.athma.sadhane.feature.todoApp.ui.TodoViewModelFactory
import web.athma.sadhane.core.ui.theme.SadhaneTheme

/**
 * The single Activity that hosts the entire Sadhane app.
 *
 * Responsible for:
 * - Bootstrapping the dependency graph (database → DAO → repository → ViewModel factory)
 * - Providing the [CoreViewModel] shared across all feature screens
 * - Rendering the [AppAlertDialog] overlay at the top level so it can appear over any screen
 * - Applying the [SadhaneTheme] and routing to the appropriate feature screen
 *
 * As new utility features are added, wire their dependencies here and add their screens
 * to the navigation/content area inside [setContent].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bootstrap the Todo feature dependencies
        val database = SadhaneDatabase.getInstance(applicationContext)
        val dao = database.todoDao()
        val repository = TodoRepository(dao)
        val viewModelFactory = TodoViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            // CoreViewModel is scoped to the Activity and shared with all feature screens
            val coreViewModel: CoreViewModel = viewModel()

            // AppAlertDialog sits above all screens to render confirmation dialogs app-wide
            AppAlertDialog(state = coreViewModel.dialogState)

            SadhaneTheme {
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
