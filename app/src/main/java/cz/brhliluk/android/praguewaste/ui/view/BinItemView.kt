package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin

@Composable
fun BinItemView(userLocation: LatLng, bin: Bin, onClick: (Bin) -> Unit) {
    Column (Modifier.clickable {
        onClick(bin)
    }) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_trash_can_solid),
                contentDescription = "Trash can icon",
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                bin.address, maxLines = 2, modifier = Modifier
                    .padding(end = 12.dp)
                    .width(200.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "+${String.format("%.1f", (bin.distanceFrom(userLocation) / 1000))}km")
        }
        LazyRow (Modifier.padding(bottom = 10.dp, top = 5.dp)) {
            items(items = bin.namedTrashTypes, itemContent = { trashType ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(trashType?.color ?: Color.Transparent)
                )
            })
        }
    }

}