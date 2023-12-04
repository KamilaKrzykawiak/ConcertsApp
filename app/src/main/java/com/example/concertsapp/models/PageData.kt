package com.example.concertsapp.models

data class PageData<T> (
    var data: List<T>,
    val totalElements: Int,
    val number: Int
)