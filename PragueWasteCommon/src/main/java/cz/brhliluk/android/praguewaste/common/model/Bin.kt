package cz.brhliluk.android.praguewaste.common.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterItem
import cz.brhliluk.android.praguewaste.common.R
import cz.brhliluk.android.praguewaste.common.theme.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Bin(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val trashTypes: List<Int>,
) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String = address

    override fun getSnippet(): String = Json.encodeToString(trashTypes)

    val namedTrashTypes = trashTypes.map { TrashType.byId(it) }.sortedBy { it?.id }

    enum class TrashType(val id: Int, val color: Color, @StringRes val title: Int) {
        PAPER(0, PaperBlue, R.string.paper),
        PLASTIC(1, PlasticYellow, R.string.plastic),
        BEVERAGE_CARTONS(2, CartonsOrange, R.string.beverage_cartons),
        COLORED_GLASS(3, GlassGreen, R.string.colored_glass),
        CLEAR_GLASS(4, GlassGray, R.string.clear_glass),
        METAL(5, MetalBrown, R.string.metal),
        E_WASTE(6, EWasteRed, R.string.e_waste);

        companion object {
            fun byId(id: Int) = values().firstOrNull { it.id == id }
            val all = listOf(PAPER, PLASTIC, BEVERAGE_CARTONS, COLORED_GLASS, CLEAR_GLASS, METAL, E_WASTE)
        }
    }

    val location: LatLng get() = LatLng(latitude, longitude)
    val offsetLocation: LatLng get() = LatLng(position.latitude - 0.00012, position.longitude)

    fun distanceFrom(location: LatLng) = SphericalUtil.computeDistanceBetween(location, this.location)
}