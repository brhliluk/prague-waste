package cz.brhliluk.android.praguewaste.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

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
            .padding(bottom = if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) 80.dp else 0.dp),
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
            BaseBinListView(items, Modifier.height(300.dp)) { item ->
                itemView(item)
            }
        }
    }
}