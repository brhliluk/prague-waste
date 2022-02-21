package cz.brhliluk.android.praguewaste.api


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.model.Bin
import io.ktor.client.features.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException

class BinNearSource(
    private val location: LatLng,
    private val radius: Float,
    private val filter: List<Bin.TrashType>,
    private val allRequired: Boolean,
) : PagingSource<Int, Bin>(), KoinComponent {

    private val api: WasteApi by inject()

    override fun getRefreshKey(state: PagingState<Int, Bin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Bin> {
        val nextPage = params.key ?: 1
        return try {
            val binList = api.getBins(location, radius, filter, allRequired, nextPage, params.loadSize)
            val nextKey = if (binList.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                nextPage + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = binList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (e: ClientRequestException) {
            return LoadResult.Error(e)
        } catch (e: ServerResponseException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 15
    }
}