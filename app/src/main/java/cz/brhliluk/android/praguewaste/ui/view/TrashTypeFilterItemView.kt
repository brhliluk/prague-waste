package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun TrashTypeFilterItemView(vm: MainViewModel, trashType: Bin.TrashType) {
    val coroutineScope = rememberCoroutineScope()
    val trashTypeState = vm.isTrashTypeEnabled(trashType).collectAsState(initial = true).value

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = trashTypeState,
            onCheckedChange = { coroutineScope.launch { vm.setTrashTypeEnabled(trashType, it) } },
            colors = CheckboxDefaults.colors(checkedColor = trashType.color),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(stringResource(trashType.title))
    }
}