package cz.brhliluk.android.praguewaste.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterItem
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.theme.Orange
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

    enum class TrashType(val id: Int,val color: Color, @StringRes val title: Int) {
        PAPER(0, Color.Blue, R.string.paper),
        PLASTIC(1, Color.Yellow, R.string.plastic),
        BEVERAGE_CARTONS(2, Orange, R.string.beverage_cartons),
        COLORED_GLASS(3, Color.Green, R.string.colored_glass),
        CLEAR_GLASS(4, Color.White, R.string.clear_glass),
        METAL(5, Color.Black, R.string.metal),
        E_WASTE(6, Color.Red, R.string.e_waste);

        companion object {
            fun byId(id: Int) = values().firstOrNull { it.id == id }
            val all = listOf(PAPER, PLASTIC, BEVERAGE_CARTONS, COLORED_GLASS, CLEAR_GLASS, METAL, E_WASTE)
        }
    }

    val location: LatLng get() = LatLng(latitude, longitude)

    fun distanceFrom(location: LatLng) = SphericalUtil.computeDistanceBetween(location, this.location)
}