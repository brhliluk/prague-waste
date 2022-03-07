package cz.brhliluk.android.praguewaste.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.brhliluk.android.praguewaste.model.Bin

@Dao
interface BinDao {
    @Query("SELECT * FROM bins_table")
    suspend fun getAll(): List<Bin>

    @Query("SELECT * FROM bins_table WHERE id IN (:binIds)")
    suspend fun loadAllByIds(binIds: IntArray): List<Bin>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(bins: List<Bin>)
}

@Dao
interface BinFtsDao {
    @Query("SELECT bins_table.* FROM bins_table join bins_fts_table on bins_fts_table.rowid = id WHERE bins_fts_table MATCH :allTypes")
    suspend fun filteredBins(allTypes: String): List<Bin>
}