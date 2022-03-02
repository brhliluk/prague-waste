package cz.brhliluk.android.praguewaste.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import cz.brhliluk.android.praguewaste.utils.location
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
    val trashTypes: List<Int>,
) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String = address

    override fun getSnippet(): String? = null

    val namedTrashTypes = trashTypes.map { TrashType.byId(it) }

    enum class TrashType(val id: Int) {
        PAPER(0),
        PLASTIC(1),
        BEVERAGE_CARTONS(2),
        COLORED_GLASS(3),
        CLEAR_GLASS(4),
        METAL(5);

        companion object {
            fun byId(id: Int) = values().firstOrNull { it.id == id }
            val all = listOf(PAPER, PLASTIC, BEVERAGE_CARTONS, COLORED_GLASS, CLEAR_GLASS, METAL)
        }
    }
}