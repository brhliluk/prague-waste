package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun BottomSearchView(vm: MainViewModel) {
    val binListItems: LazyPagingItems<Bin> = vm.searchBins.collectAsLazyPagingItems()
    var listVisible by remember { mutableStateOf(false) }

    val userLocation = vm.location.collectAsState()

    BaseBottomView(
        items = vm.searchBins.collectAsLazyPagingItems(),
        listVisible = listVisible,
        itemView = { item -> BinItemView(userLocation = userLocation.value, bin = item) }
    ) {
        TextField(
            value = vm.searchQuery,
            onValueChange = { vm.searchQuery = it },
            singleLine = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    Modifier.clickable {
                        listVisible = true
                        binListItems.refresh()
                    }
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                listVisible = true
                binListItems.refresh()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
    }
}
