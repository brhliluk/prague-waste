package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun BottomSearchView(vm: MainViewModel) {
    val binListItems: LazyPagingItems<Bin> = vm.searchBins.collectAsLazyPagingItems()
    var listVisible by remember { mutableStateOf(false) }

    Column() {
        TextField(
            value = vm.searchQuery,
            onValueChange = { vm.searchQuery = it },
            singleLine = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    Modifier.clickable { listVisible = true }
                )
            },
            keyboardActions = KeyboardActions(onDone = { listVisible = true }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
        if (listVisible) {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(binListItems) { item ->
                    item?.let {
                        // TODO: some sort of nicer card with distance etc
                        Text(text = item.address)
                    }
                }
            }
        }
    }
}
