package cz.brhliluk.android.praguewaste.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinEntity

@Dao
interface BinDao {
    @Query("SELECT * FROM bins_table")
    suspend fun getAll(): List<BinEntity>

    @Query("SELECT * FROM bins_table WHERE id IN (:binIds)")
    suspend fun loadAllByIds(binIds: IntArray): List<BinEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(bins: List<BinEntity>)
}

@Dao
interface BinFtsDao {
    @Query("SELECT bins_table.* FROM bins_table join bins_fts_table on bins_fts_table.rowid = id WHERE bins_fts_table MATCH :allTypes")
    suspend fun filteredBins(allTypes: String): List<BinEntity>

    suspend fun getFilteredBins(types: List<Bin.TrashType>, allRequired: Boolean): List<BinEntity> {
        val separator = if (allRequired) " " else " OR "
        val convertedTypes = types.map { if (it != types.last()) "${it.id}$separator" else "${it.id}" }
        return filteredBins(convertedTypes.joinToString(""))
    }

}