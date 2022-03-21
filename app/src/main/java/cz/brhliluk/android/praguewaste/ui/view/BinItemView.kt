package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import cz.brhliluk.android.praguewaste.common.model.Bin

@Composable
fun BinItemView(userLocation: LatLng, bin: Bin, onClick: (Bin) -> Unit) {
    Card(
        Modifier
            .padding(5.dp)
            .clickable {
                onClick(bin)
            },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_trash_can_solid),
                    contentDescription = "Trash can icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    bin.address, maxLines = 2, modifier = Modifier
                        .padding(end = 12.dp)
                        .width(200.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "+${String.format("%.1f", (bin.distanceFrom(userLocation) / 1000))}km")
            }
            LazyRow(Modifier.padding(top = 5.dp)) {
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
}