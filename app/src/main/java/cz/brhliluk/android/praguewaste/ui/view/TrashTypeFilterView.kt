package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun TrashTypeFilterView(vm: MainViewModel, open: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {

    if (open) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.trash_type_selection)) },
            text = {
                LazyColumn {
                    items(items = Bin.TrashType.all, itemContent = { trashType ->
                        TrashTypeFilterItemView(vm, trashType)
                    })
                }
            },
            buttons = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.ok)) } },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        )
    }
}