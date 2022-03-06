package cz.brhliluk.android.praguewaste.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import cz.brhliluk.android.praguewaste.database.BinDao
import cz.brhliluk.android.praguewaste.database.BinFtsDao
import cz.brhliluk.android.praguewaste.model.Bin
import org.koin.core.component.KoinComponent

class BinRepository(private val binDao: BinDao, private val binFtsDao: BinFtsDao) : KoinComponent {

    suspend fun insertDataAsync(bins: List<Bin>) = binDao.insertAll(bins)

    suspend fun getListAsync() = binDao.getAll()

    // I hate room so much
    // TODO: refactor this
    suspend fun getFilteredBins(types: List<Bin.TrashType>, allRequired: Boolean): List<Bin> {
        return if (!allRequired) {
            var baseQuery = "\'"
            for (type in types) {
                baseQuery += if (type != types.last()) "${type.id} OR "
                else "${type.id}\'"
            }
            binFtsDao.filteredBins(baseQuery)
        } else {
            var baseQuery = ""
            for (type in types) {
                baseQuery += if (type != types.last()) "${type.id} "
                else "${type.id}"
            }
            binFtsDao.filteredBins(baseQuery)
        }

    }
}