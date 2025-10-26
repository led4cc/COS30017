package com.example.musicrentalapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val name: String,
    val pricePerMonth: Int,
    val rating: Float,
    val categories: List<String>,   // 👈 changed from String to List<String>
    val imageRes: Int
) : Parcelable
