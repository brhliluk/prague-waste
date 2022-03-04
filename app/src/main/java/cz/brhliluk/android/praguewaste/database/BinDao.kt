package cz.brhliluk.android.praguewaste.database

import androidx.room.*
import cz.brhliluk.android.praguewaste.model.Bin

@Dao
interface BinDao {
    @Query("SELECT * FROM bins_table")
    suspend fun getAll(): List<Bin>

    @Query("SELECT * FROM bins_table WHERE id IN (:binIds)")
    fun loadAllByIds(binIds: IntArray): List<Bin>

    @Insert
    suspend fun insertAll(bins: List<Bin>)
}