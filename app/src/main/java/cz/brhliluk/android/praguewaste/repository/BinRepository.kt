package cz.brhliluk.android.praguewaste.repository

import cz.brhliluk.android.praguewaste.database.BinDao
import cz.brhliluk.android.praguewaste.model.Bin
import org.koin.core.component.KoinComponent

class BinRepository(private val binDao: BinDao): KoinComponent {

    suspend fun insertDataAsync(bins : List<Bin>) = binDao.insertAll(bins)

    suspend fun getListAsync() = binDao.getAll()
}