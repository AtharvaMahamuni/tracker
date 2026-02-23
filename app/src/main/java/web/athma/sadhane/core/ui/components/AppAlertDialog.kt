package web.athma.sadhane.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * A reusable confirmation dialog composable driven by [DialogState].
 *
 * Renders nothing when [state] is [DialogState.Hidden]. When [state] is [DialogState.Visible],
 * it shows a Material3 [AlertDialog] with confirm and cancel buttons.
 *
 * Intended to be placed at the top level of the composition (e.g. in [MainActivity]) so it
 * can overlay any screen in the app.
 *
 * @param state The current dialog state managed by [CoreViewModel].
 */
@Composable
fun AppAlertDialog(state: DialogState) {
    if (state is DialogState.Visible) {
        AlertDialog(
            onDismissRequest = state.onDismiss,
            title = { Text(state.title) },
            text = { Text(state.message) },
            confirmButton = {
                TextButton(onClick = state.onConfirm) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = state.onDismiss) { Text("Cancel") }
            }
        )
    }
}
