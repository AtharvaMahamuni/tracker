package web.athma.sadhane.core.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * App-wide ViewModel that manages shared UI state across all features.
 *
 * Currently handles the global confirmation dialog via [dialogState].
 * Any feature screen that needs to show a destructive-action confirmation should
 * call [showConfirmation] rather than managing its own dialog state.
 *
 * This ViewModel is scoped to the Activity and shared between all composable screens.
 */
class CoreViewModel : ViewModel() {

    /**
     * The current state of the app-wide dialog.
     * Observed by [AppAlertDialog] to show or hide the dialog.
     */
    var dialogState by mutableStateOf<DialogState>(DialogState.Hidden)
        private set

    /**
     * Shows a confirmation dialog with the given content.
     *
     * After the user confirms, [onConfirm] is invoked and the dialog is automatically dismissed.
     * If the user cancels, the dialog is dismissed without calling [onConfirm].
     *
     * @param title The dialog heading.
     * @param message The body text describing the action.
     * @param onConfirm Callback invoked when the user taps "Confirm".
     * @param onDismiss Optional callback invoked when the user taps "Cancel" or dismisses the dialog.
     */
    fun showConfirmation(
        title: String,
        message: String,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit = {}
    ) {
        dialogState = DialogState.Visible(
            title = title,
            message = message,
            onConfirm = {
                onConfirm()
                dialogState = DialogState.Hidden
            },
            onDismiss = {
                dismissDialog()
            }
        )
    }

    /**
     * Hides the dialog without triggering any callbacks.
     */
    fun dismissDialog() {
        dialogState = DialogState.Hidden
    }
}
