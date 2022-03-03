package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun BottomNearView(vm: MainViewModel) {

    val userLocation = vm.location.collectAsState()

    BaseBottomView(
        items = vm.nearBins.collectAsLazyPagingItems(),
        itemView = { item -> BinItemView(userLocation = userLocation.value, bin = item) }
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.proximity), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Text("+${vm.radius}km")
        }
        Slider(
            value = vm.radius, onValueChange = { vm.radius = it }, colors = SliderDefaults.colors(
                thumbColor = Color.LightGray,
                activeTrackColor = Color.LightGray
            )
        )
    }
}
