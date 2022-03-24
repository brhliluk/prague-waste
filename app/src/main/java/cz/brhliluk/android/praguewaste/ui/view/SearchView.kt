package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.navigationBarsWithImePadding
import cz.brhliluk.android.praguewaste.common.model.BinModel
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchView(vm: MainViewModel = getViewModel()) {
    val binListItems: LazyPagingItems<BinModel> = vm.searchBins.collectAsLazyPagingItems()
    var listVisible by remember { mutableStateOf(false) }

    val userLocation by vm.location.collectAsState()

    LaunchedEffect(vm.trashTypesFilter, vm.allParamsRequired, userLocation) { binListItems.refresh() }

    BottomOrSheetView(
        items = binListItems,
        listVisible = listVisible,
        itemView = { item -> BinItemView(userLocation = userLocation, bin = item.toBin(), onClick = { vm.selectBin(item.toBin()) }) }
    ) {
        TextField(
            value = vm.searchQuery,
            onValueChange = { vm.searchQuery = it },
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    Modifier.clickable {
                        listVisible = true
                        binListItems.refresh()
                    }
                )
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    listVisible = true
                    binListItems.refresh()
                },
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .navigationBarsWithImePadding()
        )
    }
}
