package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.ui.theme.BottomSheetBackground
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun BottomNearView(vm: MainViewModel) {
    val binListItems: LazyPagingItems<Bin> = vm.nearBins.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.background(BottomSheetBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .width(25.dp)
                .height(2.dp)
                .background(Color.Black)
                .clip(RoundedCornerShape(10.dp))
                .padding(top = 40.dp, bottom = 20.dp)
        )
        Row(Modifier.fillMaxWidth()) {
            Text("Vzdálenost", fontSize = 15.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text("+${vm.radius}km")
        }
        Slider(value = vm.radius, onValueChange = { vm.radius = it })
        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(binListItems) { item ->
                item?.let {
                    // TODO: some sort of nicer card with distance etc
                    Text(text = item.address)
                }
            }
            // Handle Error and Loading states
            binListItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = binListItems.loadState.refresh as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                modifier = Modifier.fillParentMaxSize(),
                                onClickRetry = { retry() }
                            )
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = binListItems.loadState.append as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                onClickRetry = { retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}
