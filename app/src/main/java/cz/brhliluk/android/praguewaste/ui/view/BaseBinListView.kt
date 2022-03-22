package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import cz.brhliluk.android.praguewaste.R

@Composable
fun <T : Any> BaseBinListView(
    items: LazyPagingItems<T>,
    modifier: Modifier,
    itemView: @Composable (T) -> Unit,
) {
    LazyColumn(modifier) {
        items(items) { item ->
            item?.let {
                itemView(it)
            }
        }

        if (items.itemCount == 0 && items.loadState.append !is LoadState.Loading && items.loadState.refresh !is LoadState.Loading) {
            item {
                Text(stringResource(R.string.no_results), textAlign = TextAlign.Center)
            }
        }

        // Handle Error and Loading states
        items.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = items.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = items.loadState.append as LoadState.Error
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