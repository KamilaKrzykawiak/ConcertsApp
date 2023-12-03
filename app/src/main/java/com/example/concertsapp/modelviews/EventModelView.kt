package com.example.concertsapp.modelviews

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.concertsapp.R
import com.example.concertsapp.models.Concert
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

//wysyła request do api, w odpowiedzi dostaje dane, przypisuje do modelu Concert i przekazuje do widoku

class EventModelView {
    private var events = MutableLiveData <List<Concert>>()
    val readEvents: LiveData <List<Concert>> get() = events

    fun getEvents(appContext: Context) {

        val queue = MySingleton.getInstance(appContext).requestQueue
        val url =
            "https://app.ticketmaster.com/discovery/v2/events.json?apikey=qDPB4SA8ARvlbWPdL90LHd3oV2GsTQka"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val eventsResponse = response.getJSONObject("_embedded").getJSONArray("events")
                val typeToken = object : TypeToken<List<Concert>>() {}.type
                val json = Gson().fromJson<List<Concert>>(eventsResponse.toString(), typeToken)
                events.value = json
//                println(eventsResponse)
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
}


//podpiac gita
// do dorobienia
// padding w całej liście lazycośtam
// lazy grid, znalezc metodę, która wywołuje sie w momencie doładowania danych
