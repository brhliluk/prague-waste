package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun BottomNearView(vm: MainViewModel) {
    val binListItems: LazyPagingItems<Bin> = vm.nearBins.collectAsLazyPagingItems()

    Column() {
        Slider(value = vm.radius, onValueChange = { vm.radius = it })
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(binListItems) { item ->
                item?.let {
                    // TODO: some sort of nicer card with distance etc
                    Text(text = item.address)
                }
            }
        }
    }
}
