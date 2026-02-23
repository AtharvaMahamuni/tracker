package web.athma.tracker.core.ui.components

sealed class DialogState {
    object Hidden : DialogState()
    data class Visible(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogState()
}