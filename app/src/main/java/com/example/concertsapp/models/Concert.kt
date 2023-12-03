package com.example.concertsapp.models

data class Concert(
    val id : String,
    val name : String,
    val type : String,
    val info : String,
  //  val dates : String,
    val images : List<Image>
)
