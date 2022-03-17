package cz.brhliluk.android.praguewaste.common.repository

import cz.brhliluk.android.praguewaste.common.database.BinDao
import cz.brhliluk.android.praguewaste.common.database.BinFtsDao
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinEntity
import org.koin.core.component.KoinComponent

class BinRepository(private val binDao: BinDao, private val binFtsDao: BinFtsDao) : KoinComponent {

    suspend fun insertDataAsync(bins: List<Bin>) = binDao.insertAll(bins.map { BinEntity.from(it) })

    suspend fun getListAsync() = binDao.getAll()

    suspend fun getFilteredBins(types: List<Bin.TrashType>, allRequired: Boolean): List<Bin> {
        val separator = if (allRequired) " " else " OR "
        val convertedTypes = types.map { if (it != types.last()) "${it.id}$separator" else "${it.id}" }
        return binFtsDao.filteredBins(convertedTypes.joinToString("")).map { it.toBin() }
    }
}