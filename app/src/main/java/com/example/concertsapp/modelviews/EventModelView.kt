package com.example.concertsapp.modelviews

import android.content.Context
import android.icu.text.StringSearch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.concertsapp.dataAccess.EventsDataAccess
import com.example.concertsapp.models.Event
import kotlinx.coroutines.flow.Flow

//wysyła request do api, w odpowiedzi dostaje dane, przypisuje do modelu Concert i przekazuje do widoku

class EventModelView(private val appContext: Context) : ViewModel() {
    var eventsDataAccess = EventsDataAccess(appContext)
    val eventsData: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 15),
        initialKey = 0,
        pagingSourceFactory = { eventsDataAccess }
    ).flow.cachedIn(viewModelScope)

    fun setSearch(search: String){
        this.eventsDataAccess = EventsDataAccess(appContext)
        this.eventsDataAccess.setQuerySearch(search)
    }

}


