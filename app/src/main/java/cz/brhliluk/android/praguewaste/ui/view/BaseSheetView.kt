package cz.brhliluk.android.praguewaste.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> BaseSheetView(
    items: LazyPagingItems<T>,
    listVisible: Boolean = true,
    itemView: @Composable (T) -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .width(400.dp)
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        if (listVisible) {
            BaseBinListView(
                items,
                if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) Modifier.height(300.dp) else Modifier.fillMaxHeight()
            ) { item ->
                itemView(item)
            }
        }
    }
}