package cz.brhliluk.android.praguewaste.common.api


import android.util.Log
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.model.BinModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent

class WasteApi : KoinComponent {

    companion object {
        private const val TIME_OUT = 60_000
        const val NETWORK_PAGE_SIZE = 15
    }

    private val ktorClient = HttpClient(Android) {
        defaultRequest {
            host = "prague-waste-api.herokuapp.com"
            url {
                protocol = URLProtocol.HTTPS
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }

            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    /**
     * Gets bins by address string similarity from API
     * @param query search query
     * @param filter TrashType filter
     * @param allRequired whether all items from filter are required
     * @param page results page
     * @param perPage how many results per page
     * @return bins by address string similarity, matching filters
     */
    suspend fun getBins(
        query: String,
        filter: List<Bin.TrashType>? = null,
        allRequired: Boolean? = null,
        page: Int? = null,
        perPage: Int? = null
    ): List<BinModel> = ktorClient.get(path = "bins-search") {
        parameter("searchQuery", query)
        parameter("filter", filter?.map { it.id })
        parameter("allRequired", allRequired)
        parameter("page", page)
        parameter("perPage", perPage)
    }

    /**
     * Gets nearest from API
     * @param location reference location where distance is calculated from
     * @param radius limitation for searching, in kms
     * @param filter TrashType filter
     * @param allRequired whether all items from filter are required
     * @param page results page
     * @param perPage how many results per page
     * @return nearest bins, matching filters
     */
    suspend fun getBins(
        location: LatLng,
        radius: Float? = null,
        filter: List<Bin.TrashType>? = null,
        allRequired: Boolean? = null,
        page: Int? = null,
        perPage: Int? = null
    ): List<BinModel> = ktorClient.get(path = "bins") {
        parameter("lat", location.latitude)
        parameter("long", location.longitude)
        parameter("radius", radius)
        parameter("filter", filter?.map { it.id })
        parameter("allRequired", allRequired)
        parameter("page", page)
        parameter("perPage", perPage)
    }

    /**
     * Returns all bins in API database
     * @return all bins
     */
    suspend fun getAllBins(): List<BinModel> = ktorClient.get(path = "bins-all")

}