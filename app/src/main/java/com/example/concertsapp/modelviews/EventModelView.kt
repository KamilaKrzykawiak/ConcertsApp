package com.example.concertsapp.modelviews

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.concertsapp.R
import com.example.concertsapp.dataAccess.EventsDataAccess
import com.example.concertsapp.models.Concert
import com.example.concertsapp.models.PageData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

//wysyła request do api, w odpowiedzi dostaje dane, przypisuje do modelu Concert i przekazuje do widoku

class EventModelView(private val appContext: Context) : ViewModel() {

    val eventsData: Flow<PagingData<Concert>> = Pager(
        config = PagingConfig(pageSize = 15),
        initialKey = 0,
        pagingSourceFactory = { EventsDataAccess(appContext) }
    ).flow.cachedIn(viewModelScope)

}


//dorobić tosta w events data access
//zmienić bottom app bar na bottom navigation
//zrobic imagesDataAccess na podstawie eventsdataaccess - skopiować requesta "get events" i "loada"
