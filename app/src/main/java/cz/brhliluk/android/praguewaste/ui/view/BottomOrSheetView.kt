package cz.brhliluk.android.praguewaste.ui.view

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T: Any> BottomOrSheetView(
    items: LazyPagingItems<T>,
    listVisible: Boolean = true,
    itemView: @Composable (T) -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> BaseSheetView(items, listVisible, itemView, content)
        else -> BaseBottomView(items, listVisible, itemView, content)
    }
}