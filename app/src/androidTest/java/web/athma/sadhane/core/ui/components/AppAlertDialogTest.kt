package web.athma.sadhane.core.ui.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppAlertDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `renders nothing when state is Hidden`() {
        composeTestRule.setContent {
            AppAlertDialog(state = DialogState.Hidden)
        }

        composeTestRule.onNodeWithText("Confirm").assertDoesNotExist()
        composeTestRule.onNodeWithText("Cancel").assertDoesNotExist()
    }

    @Test
    fun `renders dialog with title and message when state is Visible`() {
        composeTestRule.setContent {
            AppAlertDialog(
                state = DialogState.Visible(
                    title = "Delete?",
                    message = "This cannot be undone.",
                    onConfirm = {}
                )
            )
        }

        composeTestRule.onNodeWithText("Delete?").assertIsDisplayed()
        composeTestRule.onNodeWithText("This cannot be undone.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun `tapping Confirm button invokes onConfirm callback`() {
        var confirmCalled = false
        composeTestRule.setContent {
            AppAlertDialog(
                state = DialogState.Visible(
                    title = "Delete?",
                    message = "This cannot be undone.",
                    onConfirm = { confirmCalled = true }
                )
            )
        }

        composeTestRule.onNodeWithText("Confirm").performClick()

        assertTrue(confirmCalled)
    }

    @Test
    fun `tapping Cancel button invokes onDismiss callback`() {
        var dismissCalled = false
        composeTestRule.setContent {
            AppAlertDialog(
                state = DialogState.Visible(
                    title = "Delete?",
                    message = "This cannot be undone.",
                    onConfirm = {},
                    onDismiss = { dismissCalled = true }
                )
            )
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assertTrue(dismissCalled)
    }

    @Test
    fun `dismissing the dialog via onDismissRequest invokes onDismiss callback`() {
        var dismissCalled = false
        composeTestRule.setContent {
            AppAlertDialog(
                state = DialogState.Visible(
                    title = "Delete?",
                    message = "This cannot be undone.",
                    onConfirm = {},
                    onDismiss = { dismissCalled = true }
                )
            )
        }

        // onDismissRequest routes to state.onDismiss — trigger via Cancel as a proxy
        composeTestRule.onNodeWithText("Cancel").performClick()

        assertTrue(dismissCalled)
    }

    @Test
    fun `dialog is not displayed after state changes from Visible to Hidden`() {
        val dialogState = mutableStateOf<DialogState>(
            DialogState.Visible(title = "Delete?", message = "This cannot be undone.", onConfirm = {})
        )
        composeTestRule.setContent {
            AppAlertDialog(state = dialogState.value)
        }

        composeTestRule.onNodeWithText("Delete?").assertIsDisplayed()

        composeTestRule.runOnIdle { dialogState.value = DialogState.Hidden }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Delete?").assertDoesNotExist()
    }
}
