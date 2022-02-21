package cz.brhliluk.android.praguewaste.api

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.model.Bin
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

    suspend fun getBins(
        query: String,
        filter: List<Bin.TrashType>? = null,
        allRequired: Boolean? = null,
        page: Int? = null,
        perPage: Int? = null
    ): List<Bin> = ktorClient.get(path = "bins-search") {
        parameter("query", query)
        parameter("filter", filter?.map { it.id })
        parameter("allRequired", allRequired)
        parameter("page", page)
        parameter("perPage", perPage)
    }

    suspend fun getBins(
        location: LatLng,
        radius: Float? = null,
        filter: List<Bin.TrashType>? = null,
        allRequired: Boolean? = null,
        page: Int? = null,
        perPage: Int? = null
    ): List<Bin> = ktorClient.get(path = "bins") {
        parameter("lat", location.latitude)
        parameter("long", location.longitude)
        parameter("radius", radius)
        parameter("filter", filter?.map { it.id })
        parameter("allRequired", allRequired)
        parameter("page", page)
        parameter("perPage", perPage)
    }
}