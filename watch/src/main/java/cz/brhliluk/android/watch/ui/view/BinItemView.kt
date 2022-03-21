package cz.brhliluk.android.watch.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import cz.brhliluk.android.praguewaste.common.model.Bin

@Composable
fun BinItemView(bin: Bin, onClick: (Bin) -> Unit) {
    Chip(
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Trash can icon",
                modifier = Modifier.padding(end = 12.dp)
            )
        },
        onClick = { onClick(bin) },
        label = { Text(bin.address, overflow = TextOverflow.Ellipsis, maxLines = 1) },
        secondaryLabel = {
            LazyRow(Modifier.padding(bottom = 10.dp, top = 5.dp)) {
                items(items = bin.namedTrashTypes, itemContent = { trashType ->
                    Text("●", overflow = TextOverflow.Visible, color = trashType?.color ?: Color.Transparent)
                })
            }
        }
    )
}