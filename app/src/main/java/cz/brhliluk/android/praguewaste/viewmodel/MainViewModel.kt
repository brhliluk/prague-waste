package cz.brhliluk.android.praguewaste.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.api.BinSource
import cz.brhliluk.android.praguewaste.api.WasteApi
import cz.brhliluk.android.praguewaste.model.Bin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val api: WasteApi by inject()

    val bin: Flow<PagingData<Bin>> = Pager(PagingConfig(pageSize = 15)) {
        BinSource(searchQuery, filter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    private val centrePrague = LatLng(50.073658, 14.418540)
    val location = MutableStateFlow(centrePrague)
    val allParamsRequired by mutableStateOf(false)
    val filter by mutableStateOf(Bin.TrashType.all)
    var searchQuery by mutableStateOf("")

    val currentBins = MutableStateFlow<List<Bin>>(listOf())

    fun getNearestBins() = viewModelScope.launch {
        currentBins.value = api.getBins(location.value)
    }
}