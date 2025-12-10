package com.example.practica3bookstore.data

import androidx.annotation.DrawableRes

data class Book(
    val title: String,
    val author: String,
    val synopsis: String,
    @DrawableRes val image: Int,
    val price: String
)