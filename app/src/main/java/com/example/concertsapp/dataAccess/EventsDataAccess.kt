package com.example.concertsapp.dataAccess

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.concertsapp.models.Concert
import com.example.concertsapp.models.PageData
import com.example.concertsapp.modelviews.MySingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class EventsDataAccess(private val appContext: Context) : PagingSource<Int, Concert>() {
    private var events = MutableLiveData<PageData<Concert>>()
    val readEvents: LiveData<PageData<Concert>> get() = events

    private suspend fun getEvents(page: Int) = suspendCoroutine<PageData<Concert>> { cont ->

        val queue = MySingleton.getInstance(this.appContext).requestQueue
        val url =
            "https://app.ticketmaster.com/discovery/v2/events.json?apikey=qDPB4SA8ARvlbWPdL90LHd3oV2GsTQka&size=21&page=${page}"
        //defaultowo 20 pozycji &size=21 pozwoliło na zapełnienie strony

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val eventsResponse = response.getJSONObject("_embedded").getJSONArray("events")//tutaj lista 15 eventów
                val typeToken = object : TypeToken<List<Concert>>() {}.type
                val json = Gson().fromJson<List<Concert>>(eventsResponse.toString(), typeToken)
                val pageData = Gson().fromJson<PageData<Concert>>(
                    response.getJSONObject("page").toString(),
                    PageData::class.java
                )
                pageData.data = json
                events.value = pageData
//                println(pageData) //jakby dalej się zacinało
                cont.resume(pageData)

                //               println(pageData)
//                println(json.get(0).images)
            },
            { error -> //toast z info, że wystąpił błąd
                println("not working")
                // Handle error
            }
        )


// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(appContext).addToRequestQueue(jsonObjectRequest)

    }

    override fun getRefreshKey(state: PagingState<Int, Concert>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Concert> {
        try {
            val currentPageNumber = params.key ?: 0

            val data = this.getEvents(currentPageNumber)

            val events: PageData<Concert> = data

            val nextKey = when {
                (params.loadSize * (currentPageNumber + 1)) < (data.totalElements
                    ?: 0) -> currentPageNumber + 1
                else -> null
            }
                return PagingSource.LoadResult.Page(
                    prevKey = null,
                    nextKey = nextKey,
                    data = events.data
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
