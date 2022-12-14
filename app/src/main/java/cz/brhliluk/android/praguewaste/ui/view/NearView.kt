package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.common.model.BinModel
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun NearView(vm: MainViewModel = getViewModel()) {

    val binListItems: LazyPagingItems<BinModel> = vm.nearBins.collectAsLazyPagingItems()
    val userLocation by vm.location.collectAsState()

    LaunchedEffect(vm.trashTypesFilter, vm.allParamsRequired, userLocation) { binListItems.refresh() }

    BottomOrSheetView(
        items = binListItems,
        itemView = { item -> BinItemView(userLocation = userLocation, bin = item.toBin(), onClick = { vm.selectBin(item.toBin()) }) }
    ) {
        Row() {
            Text(stringResource(R.string.proximity), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Text("+${String.format("%.1f", vm.radius)}km")
        }
        Slider(
            value = vm.radius, valueRange = 0.2f..30.0f, onValueChange = {
                vm.radius = it
                binListItems.refresh()
            }
        )
    }
}
