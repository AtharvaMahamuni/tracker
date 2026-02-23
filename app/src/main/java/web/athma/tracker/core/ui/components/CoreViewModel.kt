package web.athma.tracker.core.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CoreViewModel : ViewModel() {
    var dialogState by mutableStateOf<DialogState>(DialogState.Hidden)
        private set

    fun showConfirmation(
        title: String,
        message: String,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit = {}
    )
    {
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

    fun dismissDialog() {
        dialogState = DialogState.Hidden
    }
}