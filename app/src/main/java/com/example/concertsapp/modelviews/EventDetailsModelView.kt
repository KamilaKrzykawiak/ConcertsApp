package com.example.concertsapp.modelviews

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.concertsapp.dataAccess.EventDetailsDataAccess

class EventDetailsModelView (private val appContext: Context) : ViewModel(){

    var eventDetailsDataAccess = EventDetailsDataAccess(appContext)


}