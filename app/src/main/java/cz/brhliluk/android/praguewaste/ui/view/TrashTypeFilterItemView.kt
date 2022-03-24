package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun TrashTypeFilterItemView(trashType: Bin.TrashType, vm: MainViewModel = getViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val trashTypeState by vm.isTrashTypeEnabledFlow(trashType).collectAsState(initial = true)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(trashType.title))
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = trashTypeState,
            onCheckedChange = { coroutineScope.launch { vm.setTrashTypeEnabled(trashType, it) } },
            colors = CheckboxDefaults.colors(checkedColor = trashType.color),
        )
    }
}