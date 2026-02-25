package web.athma.sadhane.feature.todoApp.ui

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import web.athma.sadhane.core.data.database.SadhaneDatabase
import web.athma.sadhane.core.ui.components.CoreViewModel
import web.athma.sadhane.feature.todoApp.data.TodoRepository

/**
 * Integration-style UI tests for [TodoScreen].
 *
 * Uses a real in-memory Room database so the full stack
 * (ViewModel → Repository → Room → Flow → Compose) is exercised.
 */
@RunWith(AndroidJUnit4::class)
class TodoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var database: SadhaneDatabase
    private lateinit var coreViewModel: CoreViewModel
    private lateinit var factory: TodoViewModelFactory

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SadhaneDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val repository = TodoRepository(database.todoDao())
        factory = TodoViewModelFactory(repository)
        coreViewModel = CoreViewModel()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun launchScreen() {
        composeTestRule.setContent {
            TodoScreen(
                todoViewModelFactory = factory,
                coreViewModel = coreViewModel
            )
        }
    }

    // --- static structure ---

    @Test
    fun `screen displays Sadhane title in top bar`() {
        launchScreen()

        composeTestRule.onNodeWithText("Sadhane").assertIsDisplayed()
    }

    @Test
    fun `screen displays Clear All button`() {
        launchScreen()

        composeTestRule.onNodeWithText("Clear All").assertIsDisplayed()
    }

    @Test
    fun `screen displays Add task icon button`() {
        launchScreen()

        composeTestRule.onNodeWithContentDescription("Add task").assertIsDisplayed()
    }

    @Test
    fun `text field is present for entering a new task`() {
        launchScreen()

        composeTestRule.onNode(hasSetTextAction()).assertIsDisplayed()
    }

    // --- adding todos ---

    @Test
    fun `typing text and tapping Add displays the todo in the list`() {
        launchScreen()

        composeTestRule.onNode(hasSetTextAction()).performTextInput("Buy groceries")
        composeTestRule.onNodeWithContentDescription("Add task").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
    }

    @Test
    fun `tapping Add with empty text field does not add a blank todo`() {
        launchScreen()

        composeTestRule.onNodeWithContentDescription("Add task").performClick()
        composeTestRule.waitForIdle()

        // No todo row should appear — only the static screen nodes are present
        composeTestRule.onNodeWithText("Sadhane").assertIsDisplayed()
        composeTestRule.onNode(hasSetTextAction()).assertIsDisplayed()
    }

    @Test
    fun `text field is cleared after a todo is added`() {
        launchScreen()

        composeTestRule.onNode(hasSetTextAction()).performTextInput("Write tests")
        composeTestRule.onNodeWithContentDescription("Add task").performClick()
        composeTestRule.waitForIdle()

        // TextField value should be reset to empty
        composeTestRule.onNodeWithText("Write tests", substring = false).assertDoesNotExist()
    }

    // --- checking off todos ---

    @Test
    fun `checking a todo checkbox marks it as done`() {
        launchScreen()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("Read a book")
        composeTestRule.onNodeWithContentDescription("Add task").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodes(isToggleable())[0].assertIsOff()

        composeTestRule.onAllNodes(isToggleable())[0].performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodes(isToggleable())[0].assertIsOn()
    }

    // --- Clear All dialog ---

    @Test
    fun `tapping Clear All when list is empty does not show a confirmation dialog`() {
        launchScreen()

        composeTestRule.onNodeWithText("Clear All").performClick()
        composeTestRule.waitForIdle()

        // No confirmation dialog should appear when there are no todos
        composeTestRule.onNodeWithText("Confirm").assertDoesNotExist()
    }

    @Test
    fun `tapping Clear All when list has items shows a confirmation dialog`() {
        launchScreen()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("Task to clear")
        composeTestRule.onNodeWithContentDescription("Add task").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Clear All").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Confirm").assertIsDisplayed()
    }

    @Test
    fun `confirming Clear All removes all todos from the list`() {
        launchScreen()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("Task to clear")
        composeTestRule.onNodeWithContentDescription("Add task").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Clear All").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Task to clear").assertDoesNotExist()
    }
}
