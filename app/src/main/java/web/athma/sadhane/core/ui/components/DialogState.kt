package web.athma.sadhane.core.ui.components

/**
 * Represents the state of an app-wide confirmation dialog.
 *
 * Use [Hidden] when no dialog should be shown. Use [Visible] to display a dialog
 * with a title, message, and confirm/dismiss callbacks.
 *
 * Observed by [AppAlertDialog] to render the dialog when [Visible].
 */
sealed class DialogState {

    /** The dialog is not shown. */
    object Hidden : DialogState()

    /**
     * The dialog is visible with the given content and callbacks.
     *
     * @property title The dialog heading shown to the user.
     * @property message The body text explaining what the action does.
     * @property onConfirm Called when the user taps the confirm button.
     * @property onDismiss Called when the user taps cancel or dismisses the dialog. Defaults to no-op.
     */
    data class Visible(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogState()
}
