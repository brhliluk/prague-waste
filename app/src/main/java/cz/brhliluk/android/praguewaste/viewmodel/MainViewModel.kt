package cz.brhliluk.android.praguewaste.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import cz.brhliluk.android.praguewaste.api.BinNearSource
import cz.brhliluk.android.praguewaste.api.BinSearchSource
import cz.brhliluk.android.praguewaste.api.WasteApi
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.repository.BinRepository
import cz.brhliluk.android.praguewaste.utils.PreferencesManager
import cz.brhliluk.android.praguewaste.utils.load
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val api: WasteApi by inject()
    private val binRepository: BinRepository by inject()
    private val preferencesManager: PreferencesManager by inject()

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
    var trashTypesFilterOpen by mutableStateOf(false)
    var trashTypesFilter by mutableStateOf(Bin.TrashType.all)
    var currentBins = mutableStateOf<List<Bin>>(listOf())

    // Might look weird, but solves clustering and map issues
    // Does not leak
    lateinit var clusterManager: ClusterManager<Bin>
    lateinit var map: GoogleMap
    private var mapViewHashCode = 0

    suspend fun initGoogleMaps(context: Context, mapView: MapView) {
        if (mapView.hashCode() == mapViewHashCode){
            return
        }
        mapViewHashCode = mapView.hashCode()
        map = mapView.awaitMap()
        clusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)

        // TODO: Update by users preference
        //noinspection MissingPermission
        map.isMyLocationEnabled = true
    }

    fun updateFilter() = loading.load {
        viewModelScope.launch {
            trashTypesFilter = preferencesManager.getTrashTypeEnabled(Bin.TrashType.all)
            allParamsRequired = preferencesManager.getUseAllEnabled()
            Log.d("ViewModel" ,"Updated filters: ${currentBins.value.size}")
            updateDisplayBins()
        }
    }

    private suspend fun updateDisplayBins() {
        currentBins.value = binRepository.getFilteredBins(trashTypesFilter, allParamsRequired)
        Log.d("ViewModel" ,"Updated currentBins: ${currentBins.value.size}")
        if (currentBins.value.isEmpty()) {
            binRepository.insertDataAsync(api.getAllBins())
            currentBins.value = binRepository.getFilteredBins(trashTypesFilter, allParamsRequired)
        }
    }

    fun isTrashTypeEnabledFlow(type: Bin.TrashType) =
        preferencesManager.getTrashTypeEnabledAsFlow(type)

    suspend fun setTrashTypeEnabled(type: Bin.TrashType, enabled: Boolean) =
        preferencesManager.setTrashType(type, enabled)

    fun isAllRequiredEnabledFlow() = preferencesManager.getUseAllEnabledAsFlow()
    suspend fun setAllRequiredEnabled(enabled: Boolean) = preferencesManager.setUseAll(enabled)
}

enum class BottomSheet {
    NONE, SEARCH, NEAR;
}