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
import cz.brhliluk.android.praguewaste.common.api.BinNearSource
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinModel
import cz.brhliluk.android.praguewaste.common.utils.LocationViewModel
import cz.brhliluk.android.praguewaste.common.utils.LocationViewModel.Companion.centrePrague
import cz.brhliluk.android.praguewaste.common.utils.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent, LocationViewModel {

    private val preferencesManager: PreferencesManager by inject()

    val nearBins: Flow<PagingData<BinModel>> = Pager(PagingConfig(pageSize = 15)) {
        BinNearSource(location.value, 30f, trashTypesFilter, allParamsRequired)
    }.flow.cachedIn(viewModelScope)

    // Bins filtering
    var allParamsRequired by mutableStateOf(false)
    var trashTypesFilter by mutableStateOf(Bin.TrashType.all)

    // Current users location
    override val location = MutableStateFlow(centrePrague)

    suspend fun isLocationEnabled() = preferencesManager.getLocationEnabled()
    fun isLocationEnabledFlow() = preferencesManager.getLocationEnabledAsFlow()
    fun setLocationEnabled(enabled: Boolean) = viewModelScope.launch { preferencesManager.setLocationEnabled(enabled) }

    val isLocationEnabled get() = runBlocking { isLocationEnabled() }
}