package cz.brhliluk.android.praguewaste.api

import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.model.Bin
import io.ktor.client.*
import io.ktor.client.request.*

class WasteApi(private val client: HttpClient) {
    suspend fun searchByAddress(
        query: String,
        filter: Array<Int>,
        allRequired: Boolean,
        page: Int,
        perPage: Int
    ): Bin = client.get(API_URL) {
        parameter("query", query)
        parameter("filter", filter)
        parameter("allRequired", allRequired)
        parameter("page", page)
        parameter("perPage", perPage)
    }

    suspend fun searchNear(
        location: LatLng,
        radius: Float,
        filter: Array<Int>,
        allRequired: Boolean,
        page: Int,
        perPage: Int
    ): Bin = client.get(API_URL) {
        parameter("lat", location.latitude)
        parameter("long", location.longitude)
        parameter("radius", radius)
        parameter("filter", filter)
        parameter("allRequired", allRequired)
        parameter("page", page)
        parameter("perPage", perPage)
    }
}