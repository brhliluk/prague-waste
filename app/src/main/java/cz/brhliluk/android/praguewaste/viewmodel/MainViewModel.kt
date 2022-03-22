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
import com.google.android.gms.maps.MapView
import cz.brhliluk.android.praguewaste.common.api.BinNearSource
import cz.brhliluk.android.praguewaste.common.api.BinSearchSource
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinModel
import cz.brhliluk.android.praguewaste.common.repository.BinRepository
import cz.brhliluk.android.praguewaste.common.utils.LocationViewModel
import cz.brhliluk.android.praguewaste.common.utils.LocationViewModel.Companion.centrePrague
import cz.brhliluk.android.praguewaste.common.utils.PreferencesManager
import cz.brhliluk.android.praguewaste.utils.GoogleMapsHelper
import cz.brhliluk.android.praguewaste.utils.load
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent, LocationViewModel {
    private val binRepository: BinRepository by inject()
    private val preferencesManager: PreferencesManager by inject()
    private val googleMapsHelper: GoogleMapsHelper by inject()

    // Paging data sources
    val searchBins: Flow<PagingData<BinModel>> = Pager(PagingConfig(pageSize = 15)) {
        BinSearchSource(searchQuery, trashTypesFilter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    val nearBins: Flow<PagingData<BinModel>> = Pager(PagingConfig(pageSize = 15)) {
        BinNearSource(location.value, radius, trashTypesFilter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    // Loading progress bar
    val loading = mutableStateOf(false)

    // Current users location
    override val location = MutableStateFlow(centrePrague)

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

    // Google Maps
    suspend fun initGoogleMaps(mapView: MapView) = googleMapsHelper.initGoogleMaps(mapView)
    fun setMyLocationEnabled() = viewModelScope.launch { googleMapsHelper.setMyLocationEnabled() }
    fun selectBin(bin: Bin) = viewModelScope.launch { googleMapsHelper.selectBin(bin) }
    fun replaceBins() = googleMapsHelper.replaceItems(currentBins.value)

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
            binRepository.loadAllBins()
            currentBins.value = binRepository.getFilteredBins(trashTypesFilter, allParamsRequired)
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
}

enum class BottomSheet {
    NONE, SEARCH, NEAR;
}