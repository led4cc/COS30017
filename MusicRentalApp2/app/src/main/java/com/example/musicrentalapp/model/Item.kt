// model/Item.kt
package com.example.musicrentalapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val name: String,
    val pricePerMonth: Int,           // treat as “credit”
    val rating: Float,                // 0–5
    val category: String,             // multi-choice attribute
    val imageRes: Int
) : Parcelable
