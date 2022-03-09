package cz.brhliluk.android.praguewaste.viewmodel

import android.content.Context
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
import cz.brhliluk.android.praguewaste.api.BinNearSource
import cz.brhliluk.android.praguewaste.api.BinSearchSource
import cz.brhliluk.android.praguewaste.api.WasteApi
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.repository.BinRepository
import cz.brhliluk.android.praguewaste.utils.InfoWindowAdapter
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
    private val infoWindowAdapter: InfoWindowAdapter by inject()

    // Paging data sources
    val searchBins: Flow<PagingData<Bin>> = Pager(PagingConfig(pageSize = 15)) {
        BinSearchSource(searchQuery, trashTypesFilter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    val nearBins: Flow<PagingData<Bin>> = Pager(PagingConfig(pageSize = 15)) {
        BinNearSource(location.value, radius, trashTypesFilter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    // Loading progress bar
    val loading = mutableStateOf(false)

    // Current users location
    val location = MutableStateFlow(centrePrague)

    // Bins filtering
    var allParamsRequired by mutableStateOf(false)
    var trashTypesFilter by mutableStateOf(Bin.TrashType.all)
    var currentBins = mutableStateOf<List<Bin>>(listOf())

    // API params connected to UI
    var searchQuery by mutableStateOf("")
    var radius by mutableStateOf(500F)

    // UI states
    var activeBottomSheet by mutableStateOf(BottomSheet.NONE)
    var trashTypesFilterOpen by mutableStateOf(false)

    // Might look weird, but solves clustering and map issues related to lifecycle
    // and unnecessary recompositions
    // Does not leak
    lateinit var clusterManager: ClusterManager<Bin>
    lateinit var map: GoogleMap
    private var mapViewHashCode = 0

    suspend fun initGoogleMaps(context: Context, mapView: MapView) {
        // No unnecessary new initializations
        if (mapView.hashCode() == mapViewHashCode) return
        mapViewHashCode = mapView.hashCode()
        map = mapView.awaitMap()
        clusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
        clusterManager.markerCollection.setInfoWindowAdapter(infoWindowAdapter)
        // TODO: Update by users preference
        //noinspection MissingPermission
        map.isMyLocationEnabled = true
    }

    fun updateFilters() = loading.load {
        viewModelScope.launch {
            trashTypesFilter = preferencesManager.getTrashTypeEnabled(Bin.TrashType.all)
            allParamsRequired = preferencesManager.getUseAllEnabled()
            updateDisplayBins()
        }
    }

    private suspend fun updateDisplayBins() {
        currentBins.value = binRepository.getFilteredBins(trashTypesFilter, allParamsRequired)
        if (currentBins.value.isEmpty()) {
            binRepository.insertDataAsync(api.getAllBins())
            currentBins.value = binRepository.getFilteredBins(trashTypesFilter, allParamsRequired)
        }
    }

    // Access to prefs through manager
    fun isTrashTypeEnabledFlow(type: Bin.TrashType) = preferencesManager.getTrashTypeEnabledAsFlow(type)
    suspend fun setTrashTypeEnabled(type: Bin.TrashType, enabled: Boolean) = preferencesManager.setTrashType(type, enabled)
    fun isAllRequiredEnabledFlow() = preferencesManager.getUseAllEnabledAsFlow()
    suspend fun setAllRequiredEnabled(enabled: Boolean) = preferencesManager.setUseAll(enabled)

    companion object {
        val centrePrague = LatLng(50.073658, 14.418540)
    }
}

enum class BottomSheet {
    NONE, SEARCH, NEAR;
}