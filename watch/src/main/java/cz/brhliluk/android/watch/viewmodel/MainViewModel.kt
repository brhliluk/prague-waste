package cz.brhliluk.android.watch.viewmodel

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
import cz.brhliluk.android.praguewaste.common.api.BinNearSource
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val nearBins: Flow<PagingData<BinModel>> = Pager(PagingConfig(pageSize = 15)) {
        BinNearSource(location.value, 30f, trashTypesFilter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    // Bins filtering
    var allParamsRequired by mutableStateOf(false)
    var trashTypesFilter by mutableStateOf(Bin.TrashType.all)

    // Current users location
    val location = MutableStateFlow(centrePrague)

    companion object {
        val centrePrague = LatLng(50.073658, 14.418540)
    }
}