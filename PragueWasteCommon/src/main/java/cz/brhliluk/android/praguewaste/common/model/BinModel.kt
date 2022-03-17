package cz.brhliluk.android.praguewaste.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BinModel (
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    @SerialName("trash_types")
    val trashTypes: List<Int>,
) {
    companion object {
        fun from(bin: Bin): BinModel {
            return BinModel(bin.id, bin.latitude, bin.longitude, bin.address, bin.trashTypes)
        }
    }

    fun toBin(): Bin {
        return Bin(id, latitude, longitude, address, trashTypes)
    }
}