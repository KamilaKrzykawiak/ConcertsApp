package com.example.concertsapp.dataAccess

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.concertsapp.models.EventDetails
import com.example.concertsapp.modelviews.MySingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventDetailsDataAccess(private val appContext: Context) {
    private var eventDetails = MutableLiveData<EventDetails>()
    val readEventDetail: LiveData<EventDetails> get() = eventDetails

    public fun getEventDetails(ID: String) {

        val queue = MySingleton.getInstance(this.appContext).requestQueue
        val url =
            "https://app.ticketmaster.com/discovery/v2/events/${ID}.json?apikey=qDPB4SA8ARvlbWPdL90LHd3oV2GsTQka&size=21"
        //defaultowo 20 pozycji &size=21 pozwoliło na zapełnienie strony

        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            { response ->

                val eventsResponse = response
                val typeToken = object : TypeToken<EventDetails>() {}.type
                val json = Gson().fromJson<EventDetails>(eventsResponse.toString(), typeToken)
                eventDetails.value = json
                //  println(eventsResponse[0].getJSONObject("keyword")) //jakby dalej się zacinało
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
}