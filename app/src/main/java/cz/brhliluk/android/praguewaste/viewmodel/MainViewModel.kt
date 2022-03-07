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
import cz.brhliluk.android.praguewaste.api.BinNearSource
import cz.brhliluk.android.praguewaste.api.BinSearchSource
import cz.brhliluk.android.praguewaste.api.WasteApi
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.repository.BinRepository
import cz.brhliluk.android.praguewaste.utils.load
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val api: WasteApi by inject()
    private val binRepository: BinRepository by inject()

    val searchBins: Flow<PagingData<Bin>> = Pager(PagingConfig(pageSize = 15)) {
        BinSearchSource(searchQuery, filter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    val nearBins: Flow<PagingData<Bin>> = Pager(PagingConfig(pageSize = 15)) {
        BinNearSource(location.value, radius, filter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    val loading = mutableStateOf(false)
    private val centrePrague = LatLng(50.073658, 14.418540)
    val location = MutableStateFlow(centrePrague)
    var allParamsRequired by mutableStateOf(false)
    var filter by mutableStateOf(Bin.TrashType.all)
    var searchQuery by mutableStateOf("")
    var radius by mutableStateOf(500F)
    var activeBottomSheet by mutableStateOf(BottomSheet.NONE)
    // TODO: add TrashType filters

    var currentBins = mutableStateOf<List<Bin>>(listOf())

    fun fetchAllBins() = loading.load {
        viewModelScope.launch {
            with(binRepository.getFilteredBins(listOf(Bin.TrashType.METAL, Bin.TrashType.E_WASTE), true)) {
                if (this.isNotEmpty()) currentBins.value = this
                else {
                    binRepository.insertDataAsync(api.getAllBins())
                    currentBins.value = binRepository.getFilteredBins(listOf(Bin.TrashType.E_WASTE), false)
                }
            }
        }
    }
}

enum class BottomSheet {
    NONE, SEARCH, NEAR;
}