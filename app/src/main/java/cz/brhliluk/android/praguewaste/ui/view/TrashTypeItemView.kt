package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.model.Bin

@Composable
fun TrashTypeItemView(trashType: Bin.TrashType) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(end = 12.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(trashType.color)
        )
        Text(stringResource(trashType.title))
    }
}