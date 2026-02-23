package web.athma.tracker.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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
