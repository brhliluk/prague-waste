package cz.brhliluk.android.watch.ui.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.watch.R


@Composable
fun BinView(bin: Bin, userLocation: LatLng) {
    val context = LocalContext.current

    ScalingLazyColumn(modifier = Modifier.padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Text(bin.address, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color.Black)
        }
        item {
            Text(text = "+${String.format("%.1f", (bin.distanceFrom(userLocation) / 1000))}km", color = Color.Black)
        }
        item {
            FlowRow {
                bin.namedTrashTypes.forEach { bin ->
                    if (bin != null) {
                        Text(
                            text = stringResource(bin.title).uppercase(),
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    color = bin.color.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
        item {
            Chip(
                icon = { Icon(imageVector = Icons.Filled.Place, contentDescription = "NavigateIcon") },
                label = { Text(text = stringResource(R.string.navigate)) },
                onClick = {
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${bin.latitude},${bin.longitude}"))
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(mapIntent)
                    } else {
                        Toast.makeText(context, context.getString(R.string.no_supported_app), Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color.Blue,
                    contentColor = Color.White
                )
            )
        }
    }
}