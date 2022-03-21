package cz.brhliluk.android.watch.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import cz.brhliluk.android.praguewaste.common.model.BinModel
import cz.brhliluk.android.watch.R
import cz.brhliluk.android.watch.ui.util.items
import cz.brhliluk.android.watch.viewmodel.MainViewModel

@Composable
fun MainView(vm: MainViewModel) {
    val binListItems: LazyPagingItems<BinModel> = vm.nearBins.collectAsLazyPagingItems()
    val userLocation = vm.location.collectAsState()


    ScalingLazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Text(stringResource(R.string.nearest_bins), fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
        }

        items(binListItems) { item ->
            item?.let {
                BinItemView(userLocation = userLocation.value, bin = item.toBin(), onClick = { TODO() })
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