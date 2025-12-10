package com.example.practica3bookstore.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val title: String,
    val author: String,
    val synopsis: String,
    @DrawableRes val image: Int,
    val price: String
) : Parcelable