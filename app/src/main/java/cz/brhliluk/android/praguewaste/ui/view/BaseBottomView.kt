package cz.brhliluk.android.praguewaste.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items

@Composable
fun <T : Any> BaseBottomView(
    items: LazyPagingItems<T>,
    listVisible: Boolean = true,
    itemView: @Composable (T) -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(bottom = if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) 80.dp else 0.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .padding(top = 10.dp, bottom = 20.dp)
                .width(40.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        )
        content()
        if (listVisible) {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(items) { item ->
                    item?.let {
                        itemView(it)
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
    }
}