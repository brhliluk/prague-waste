package cz.brhliluk.android.praguewaste.common.repository

import cz.brhliluk.android.praguewaste.common.api.WasteApi
import cz.brhliluk.android.praguewaste.common.database.BinDao
import cz.brhliluk.android.praguewaste.common.database.BinFtsDao
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BinRepository(private val binDao: BinDao, private val binFtsDao: BinFtsDao) : KoinComponent {
    private val api: WasteApi by inject()

    private suspend fun insertDataAsync(bins: List<Bin>) = binDao.insertAll(bins.map { BinEntity.from(it) })

    suspend fun getListAsync() = binDao.getAll()

    /**
     * Returns bins corresponding to selected filters
     * @param types required TrashTypes
     * @param allRequired whether all required TrashTypes need to be present
     */
    suspend fun getFilteredBins(types: List<Bin.TrashType>, allRequired: Boolean) =
        binFtsDao.getFilteredBins(types, allRequired).map { it.toBin() }

    /**
     * Loads all bins from API and stores them into the database
     */
    suspend fun loadAllBins() = insertDataAsync(api.getAllBins().map { it.toBin() })
}