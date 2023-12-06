package com.example.concertsapp.models

data class Event(
    val id : String,
    val name : String,
    val type : String,
    val info : String,
  //  val dates : String,
    val images : List<Image>
)
