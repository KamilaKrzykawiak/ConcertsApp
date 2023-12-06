package com.example.concertsapp.modelviews

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.concertsapp.dataAccess.AttractionsDataAccess
import com.example.concertsapp.models.AttractionImage
import kotlinx.coroutines.flow.Flow

class AttractionImageModelView (private val appContext: Context) : ViewModel() {

    val attractionsImageData: Flow<PagingData<AttractionImage>> = Pager(
        config = PagingConfig(pageSize = 15),
        initialKey = 0,
        pagingSourceFactory = { AttractionsDataAccess(appContext) }
    ).flow.cachedIn(viewModelScope)

}