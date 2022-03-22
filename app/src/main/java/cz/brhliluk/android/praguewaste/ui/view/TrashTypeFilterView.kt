package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Switch
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun TrashTypeFilterView(vm: MainViewModel = getViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val allRequired = vm.isAllRequiredEnabledFlow().collectAsState(initial = false).value

    // Update markers only after filter is closed
    fun onClose() {
        vm.updateFilters()
        vm.trashTypesFilterOpen = false
    }

    if (vm.trashTypesFilterOpen) {
        AlertDialog(
            onDismissRequest = { onClose() },
            title = { Text(stringResource(R.string.trash_type_selection)) },
            text = {
                Column {
                    Row {
                        Text(stringResource(R.string.all_selected_required))
                        Spacer(Modifier.weight(1f))
                        Switch(
                            checked = allRequired,
                            onCheckedChange = { coroutineScope.launch { vm.setAllRequiredEnabled(it) } })
                    }
                    LazyColumn {
                        items(items = Bin.TrashType.all, itemContent = { trashType ->
                            TrashTypeFilterItemView(trashType)
                        })
                    }
                }
            },
            confirmButton = { TextButton(onClick = { onClose() }) { Text(stringResource(R.string.ok)) } },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
            containerColor = Color.LightGray,
            textContentColor = Color.Black
        )
    }
}