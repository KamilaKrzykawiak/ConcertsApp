package com.example.concertsapp.modelviews

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.concertsapp.dataAccess.EventsDataAccess
import com.example.concertsapp.models.Event
import kotlinx.coroutines.flow.Flow

//wysyła request do api, w odpowiedzi dostaje dane, przypisuje do modelu Concert i przekazuje do widoku

class EventModelView(private val appContext: Context) : ViewModel() {

    val eventsData: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 15),
        initialKey = 0,
        pagingSourceFactory = { EventsDataAccess(appContext) }
    ).flow.cachedIn(viewModelScope)

}



//zmienić bottom app bar na bottom navigation
//zrobic imagesDataAccess na podstawie eventsdataaccess - skopiować requesta "get events" i "loada"
