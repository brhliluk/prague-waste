package cz.brhliluk.android.praguewaste.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.common.model.Bin
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

// Couldn't find a way to use compose here
class InfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter, KoinComponent {
    private val layoutInflater = LayoutInflater.from(context)
    private val view: View = layoutInflater.inflate(R.layout.marker_info_window, null)

    private fun setInfoWindowText(marker: Marker) {
        val trashTypesList = Json.decodeFromString<List<Int>>(marker.snippet!!).map { Bin.TrashType.byId(it) }
        view.findViewById<TextView>(R.id.markerInfoTitle).text = marker.title
        // This is stupid but databinding didn't work for me here
        setViewVisible(R.id.paper, Bin.TrashType.PAPER, trashTypesList)
        setViewVisible(R.id.plastic, Bin.TrashType.PLASTIC, trashTypesList)
        setViewVisible(R.id.beverageCartons, Bin.TrashType.BEVERAGE_CARTONS, trashTypesList)
        setViewVisible(R.id.clearGlass, Bin.TrashType.CLEAR_GLASS, trashTypesList)
        setViewVisible(R.id.coloredGlass, Bin.TrashType.COLORED_GLASS, trashTypesList)
        setViewVisible(R.id.metal, Bin.TrashType.METAL, trashTypesList)
        setViewVisible(R.id.eWaste, Bin.TrashType.E_WASTE, trashTypesList)
    }

    private fun setViewVisible(@IdRes resId: Int, type: Bin.TrashType, all: List<Bin.TrashType?>) {
        view.findViewById<ImageView>(resId).visibility = if (all.contains(type)) View.VISIBLE else View.GONE
    }

    override fun getInfoWindow(marker: Marker): View {
        setInfoWindowText(marker)
        return view
    }

    override fun getInfoContents(marker: Marker): View {
        setInfoWindowText(marker)
        return view
    }
}