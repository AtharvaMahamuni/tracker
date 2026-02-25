package web.athma.sadhane.core.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogStateTest {

    // --- Hidden ---

    @Test
    fun `Hidden is always the same singleton instance`() {
        assertSame(DialogState.Hidden, DialogState.Hidden)
    }

    @Test
    fun `Hidden is an instance of DialogState`() {
        assertTrue(DialogState.Hidden is DialogState)
    }

    // --- Visible ---

    @Test
    fun `Visible stores title and message correctly`() {
        val state = DialogState.Visible(
            title = "Delete?",
            message = "This cannot be undone.",
            onConfirm = {}
        )

        assertEquals("Delete?", state.title)
        assertEquals("This cannot be undone.", state.message)
    }

    @Test
    fun `Visible onConfirm invokes the provided lambda`() {
        var called = false
        val state = DialogState.Visible(
            title = "Title",
            message = "Message",
            onConfirm = { called = true }
        )

        state.onConfirm()

        assertTrue(called)
    }

    @Test
    fun `Visible default onDismiss is a no-op and does not throw`() {
        val state = DialogState.Visible(
            title = "Title",
            message = "Message",
            onConfirm = {}
        )

        state.onDismiss() // should not throw
    }

    @Test
    fun `Visible onDismiss invokes the provided lambda`() {
        var called = false
        val state = DialogState.Visible(
            title = "Title",
            message = "Message",
            onConfirm = {},
            onDismiss = { called = true }
        )

        state.onDismiss()

        assertTrue(called)
    }

    @Test
    fun `Visible instances with same callbacks and content are equal`() {
        val onConfirm: () -> Unit = {}
        val onDismiss: () -> Unit = {}
        val state1 = DialogState.Visible("Title", "Message", onConfirm, onDismiss)
        val state2 = DialogState.Visible("Title", "Message", onConfirm, onDismiss)

        assertEquals(state1, state2)
    }

    @Test
    fun `Visible instances with different titles are not equal`() {
        val onConfirm: () -> Unit = {}
        val state1 = DialogState.Visible("Title A", "Message", onConfirm)
        val state2 = DialogState.Visible("Title B", "Message", onConfirm)

        assertNotEquals(state1, state2)
    }

    @Test
    fun `Visible instances with different messages are not equal`() {
        val onConfirm: () -> Unit = {}
        val state1 = DialogState.Visible("Title", "Message A", onConfirm)
        val state2 = DialogState.Visible("Title", "Message B", onConfirm)

        assertNotEquals(state1, state2)
    }

    @Test
    fun `Visible is not Hidden`() {
        val state: DialogState = DialogState.Visible("T", "M", {})

        assertFalse(state is DialogState.Hidden)
    }

    @Test
    fun `Hidden is not Visible`() {
        val state: DialogState = DialogState.Hidden

        assertFalse(state is DialogState.Visible)
    }
}
