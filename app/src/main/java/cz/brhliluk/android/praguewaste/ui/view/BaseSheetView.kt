package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> BaseSheetView(
    items: LazyPagingItems<T>,
    listVisible: Boolean = true,
    itemView: @Composable (T) -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        if (listVisible) {
            BaseBinListView(items, Modifier.height(300.dp)) { item ->
                itemView(item)
            }
        }
    }
}