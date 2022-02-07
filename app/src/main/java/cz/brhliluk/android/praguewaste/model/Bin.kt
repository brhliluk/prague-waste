package cz.brhliluk.android.praguewaste.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bin(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("address")
    val address: String,
    @SerialName("trash_types")
    val totalStars: Array<Int>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bin

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (address != other.address) return false
        if (!totalStars.contentEquals(other.totalStars)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + totalStars.contentHashCode()
        return result
    }
}