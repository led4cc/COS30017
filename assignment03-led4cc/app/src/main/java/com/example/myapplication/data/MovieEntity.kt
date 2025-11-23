package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val year: Int?,
    val genre: String,
    val status: String,          // "Plan to watch", "Watching", "Watched"
    val personalRating: Float?,  // 0–10, nullable if not rated yet
    val isFavourite: Boolean,
    val dateWatched: Long?,
    val notes: String?,
    val isBuster: Boolean        // ⬅️ NEW: remind me to watch
)
