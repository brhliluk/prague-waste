package cz.brhliluk.android.praguewaste.common.model


import androidx.room.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "bins_table")
data class BinEntity(
    @PrimaryKey val id: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    @ColumnInfo(name = "trash_types")
    val trashTypes: List<Int>,
) {
    companion object {
        fun from(bin: Bin): BinEntity {
            return BinEntity(bin.id, bin.latitude, bin.longitude, bin.address, bin.trashTypes)
        }
    }

    fun toBin(): Bin {
        return Bin(id, latitude, longitude, address, trashTypes)
    }
}

class Converters {

    @TypeConverter
    fun listToJsonString(value: List<Int>?): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToList(value: String): List<Int> = Json.decodeFromString(value)
}

@Fts4(contentEntity = BinEntity::class)
@Entity(tableName = "bins_fts_table")
class BinFts(
    @ColumnInfo(name = "rowid")
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "trash_types")
    val trashTypes: String,
)