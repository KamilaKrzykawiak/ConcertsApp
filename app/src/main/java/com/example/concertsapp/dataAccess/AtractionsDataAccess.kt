package com.example.concertsapp.dataAccess

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.concertsapp.models.AttractionImage
import com.example.concertsapp.models.PageData
import com.example.concertsapp.modelviews.MySingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AttractionsDataAccess (private val appContext: Context) : PagingSource<Int, AttractionImage>() {
    private var attractions = MutableLiveData<PageData<AttractionImage>>()
    val readAttractions: LiveData<PageData<AttractionImage>> get() = attractions

    private suspend fun getAttractions(page: Int) = suspendCoroutine<PageData<AttractionImage>> { cont ->

        val queue = MySingleton.getInstance(this.appContext).requestQueue
        val url =
            "https://app.ticketmaster.com/discovery/v2/attractions.json?apikey=qDPB4SA8ARvlbWPdL90LHd3oV2GsTQka&size=21&page=${page}"
        //defaultowo 20 pozycji &size=21 pozwoliło na zapełnienie strony

        // in Activity

        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            { response ->
                val eventsResponse = response.getJSONObject("_embedded")
                    .getJSONArray("attractions")//tutaj lista 15 eventów
                val typeToken = object : TypeToken<List<AttractionImage>>() {}.type
                val json = Gson().fromJson<List<AttractionImage>>(eventsResponse.toString(), typeToken)
                val pageData = Gson().fromJson<PageData<AttractionImage>>(
                    response.getJSONObject("page").toString(),
                    PageData::class.java
                )
                pageData.data = json
                attractions.value = pageData
//                println(pageData) //jakby dalej się zacinało
                cont.resume(pageData)

                //               println(pageData)
//                println(json.get(0).images)
            },
            { error ->
                val duration = Toast.LENGTH_LONG
                val toast = Toast.makeText(appContext, "Error", duration)
                toast.show()
                // Handle error
            }
        )


// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(appContext).addToRequestQueue(jsonObjectRequest)

    }

    override fun getRefreshKey(state: PagingState<Int, AttractionImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, AttractionImage> {
        try {
            val currentPageNumber = params.key ?: 0

            val data = this.getAttractions(currentPageNumber)

            val attractions: PageData<AttractionImage> = data

            val nextKey = when {
                (params.loadSize * (currentPageNumber + 1)) < (data.totalElements
                    ?: 0) -> currentPageNumber + 1
                else -> null
            }
            return PagingSource.LoadResult.Page(
                prevKey = null,
                nextKey = nextKey,
                data = attractions.data
            )

        } catch (e: Exception) {
            return PagingSource.LoadResult.Page(
                prevKey = null,
                nextKey = null,
                data = emptyList()
            )
        }
    }
}
