package cz.brhliluk.android.watch.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.common.model.Bin

@Composable
fun BinItemView(userLocation: LatLng, bin: Bin, onClick: (Bin) -> Unit) {
    Chip(
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Trash can icon",
                modifier = Modifier.padding(end = 12.dp)
            )
        },
        onClick = { TODO() },
        label = { Text(bin.address, overflow = TextOverflow.Ellipsis, maxLines = 1) },
        secondaryLabel = {
            Text(text = "+${String.format("%.1f", (bin.distanceFrom(userLocation) / 1000))}km")
//            LazyRow(Modifier.padding(bottom = 10.dp, top = 5.dp)) {
//                items(items = bin.namedTrashTypes, itemContent = { trashType ->
//                    Box(
//                        modifier = Modifier
//                            .padding(5.dp)
//                            .size(10.dp)
//                            .clip(CircleShape)
//                            .background(trashType?.color ?: Color.Transparent)
//                    )
//                })
//            }
        }
    )
}