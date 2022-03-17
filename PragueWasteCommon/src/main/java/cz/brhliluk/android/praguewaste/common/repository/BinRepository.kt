package cz.brhliluk.android.praguewaste.common.repository

import cz.brhliluk.android.praguewaste.common.database.BinDao
import cz.brhliluk.android.praguewaste.common.database.BinFtsDao
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinEntity
import org.koin.core.component.KoinComponent

class BinRepository(private val binDao: BinDao, private val binFtsDao: BinFtsDao) : KoinComponent {

    suspend fun insertDataAsync(bins: List<Bin>) = binDao.insertAll(bins.map { BinEntity.from(it) })

    suspend fun getListAsync() = binDao.getAll()

    suspend fun getFilteredBins(types: List<Bin.TrashType>, allRequired: Boolean) =
        binFtsDao.getFilteredBins(types, allRequired).map { it.toBin() }
}