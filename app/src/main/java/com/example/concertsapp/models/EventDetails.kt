package com.example.concertsapp.models

data class EventDetails(
    val name: String,
    val id: String,
    val url: String,
    val images: List<Image>,
    val description: String,
    val additionalInfo: String,
    val info: String
)
