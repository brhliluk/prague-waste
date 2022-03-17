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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import cz.brhliluk.android.praguewaste.common.api.BinNearSource
import cz.brhliluk.android.praguewaste.common.api.BinSearchSource
import cz.brhliluk.android.praguewaste.common.api.WasteApi
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.repository.BinRepository
import cz.brhliluk.android.praguewaste.utils.InfoWindowAdapter
import cz.brhliluk.android.praguewaste.utils.PreferencesManager
import cz.brhliluk.android.praguewaste.utils.load
import cz.brhliluk.android.praguewaste.utils.offsetLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    var radius by mutableStateOf(30F)

    // UI states
    var activeBottomSheet by mutableStateOf(BottomSheet.NONE)
    var trashTypesFilterOpen by mutableStateOf(false)

    // Solves clustering and map issues related to lifecycle and unnecessary recompositions
    lateinit var clusterManager: ClusterManager<Bin>
    private lateinit var map: GoogleMap
    private var mapViewHashCode = 0

    suspend fun initGoogleMaps(context: Context, mapView: MapView) {
        // No unnecessary new initializations on recomposition
        if (mapView.hashCode() == mapViewHashCode) return
        mapViewHashCode = mapView.hashCode()
        map = mapView.awaitMap()
        map.setPadding(0,100, 0, 200)
        clusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centrePrague, 10f))
        clusterManager.markerCollection.setInfoWindowAdapter(infoWindowAdapter)
        clusterManager.setOnClusterClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, map.cameraPosition.zoom + 1), 500, null)
            true
        }
    }

    fun setMyLocationEnabled() {
        if (this::map.isInitialized) viewModelScope.launch {
            //noinspection MissingPermission
            map.isMyLocationEnabled = preferencesManager.getLocationEnabled() && preferencesManager.hasLocationPermission
        }
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

    fun selectBin(bin: Bin) {
        viewModelScope.launch {
            map.awaitAnimateCamera(CameraUpdateFactory.newLatLngZoom(bin.offsetLocation, 20.0f), 1000)
            // Give clusterManager time to load in all the markers if too far away
            delay(200)
            clusterManager.markerCollection.markers.find { it.position == bin.position }?.showInfoWindow()
        }
    }

    // Access to prefs through manager
    fun isTrashTypeEnabledFlow(type: Bin.TrashType) = preferencesManager.getTrashTypeEnabledAsFlow(type)
    suspend fun setTrashTypeEnabled(type: Bin.TrashType, enabled: Boolean) = preferencesManager.setTrashType(type, enabled)
    fun isAllRequiredEnabledFlow() = preferencesManager.getUseAllEnabledAsFlow()
    suspend fun setAllRequiredEnabled(enabled: Boolean) = preferencesManager.setUseAll(enabled)
    suspend fun isLocationEnabled() = preferencesManager.getLocationEnabled()
    fun isLocationEnabledFlow() = preferencesManager.getLocationEnabledAsFlow()
    fun setLocationEnabled(enabled: Boolean) = viewModelScope.launch { preferencesManager.setLocationEnabled(enabled) }

    val isLocationEnabled get() = runBlocking { isLocationEnabled() }

    companion object {
        val centrePrague = LatLng(50.073658, 14.418540)
    }
}

enum class BottomSheet {
    NONE, SEARCH, NEAR;
}