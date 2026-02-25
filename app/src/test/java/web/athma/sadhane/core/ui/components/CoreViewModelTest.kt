package web.athma.sadhane.core.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CoreViewModelTest {

    private lateinit var viewModel: CoreViewModel

    @Before
    fun setUp() {
        viewModel = CoreViewModel()
    }

    // --- initial state ---

    @Test
    fun `initial dialogState is Hidden`() {
        assertTrue(viewModel.dialogState is DialogState.Hidden)
    }

    // --- showConfirmation ---

    @Test
    fun `showConfirmation sets dialogState to Visible with correct title and message`() {
        viewModel.showConfirmation(
            title = "Delete?",
            message = "This cannot be undone.",
            onConfirm = {}
        )

        val state = viewModel.dialogState as DialogState.Visible
        assertEquals("Delete?", state.title)
        assertEquals("This cannot be undone.", state.message)
    }

    @Test
    fun `showConfirmation stored onConfirm invokes user callback and hides dialog`() {
        var confirmCalled = false
        viewModel.showConfirmation(
            title = "Delete?",
            message = "This cannot be undone.",
            onConfirm = { confirmCalled = true }
        )

        (viewModel.dialogState as DialogState.Visible).onConfirm()

        assertTrue(confirmCalled)
        assertTrue(viewModel.dialogState is DialogState.Hidden)
    }

    @Test
    fun `showConfirmation stored onDismiss hides dialog`() {
        viewModel.showConfirmation(
            title = "Delete?",
            message = "This cannot be undone.",
            onConfirm = {}
        )

        (viewModel.dialogState as DialogState.Visible).onDismiss()

        assertTrue(viewModel.dialogState is DialogState.Hidden)
    }

    @Test
    fun `showConfirmation can be called multiple times and last state wins`() {
        viewModel.showConfirmation(title = "First", message = "First message", onConfirm = {})
        viewModel.showConfirmation(title = "Second", message = "Second message", onConfirm = {})

        val state = viewModel.dialogState as DialogState.Visible
        assertEquals("Second", state.title)
        assertEquals("Second message", state.message)
    }

    // --- dismissDialog ---

    @Test
    fun `dismissDialog sets dialogState to Hidden`() {
        viewModel.showConfirmation(title = "Title", message = "Message", onConfirm = {})

        viewModel.dismissDialog()

        assertTrue(viewModel.dialogState is DialogState.Hidden)
    }

    @Test
    fun `dismissDialog when already Hidden does not crash`() {
        viewModel.dismissDialog()

        assertTrue(viewModel.dialogState is DialogState.Hidden)
    }

    @Test
    fun `showConfirmation after dismissDialog shows new dialog`() {
        viewModel.showConfirmation(title = "First", message = "First", onConfirm = {})
        viewModel.dismissDialog()
        viewModel.showConfirmation(title = "Second", message = "Second", onConfirm = {})

        val state = viewModel.dialogState as DialogState.Visible
        assertEquals("Second", state.title)
    }
}
