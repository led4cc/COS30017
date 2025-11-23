package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.example.myapplication.data.MovieEntity

class StatsActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    private lateinit var toolbar: MaterialToolbar

    private lateinit var summaryTextView: TextView
    private lateinit var summaryDetailTextView: TextView
    private lateinit var planTextView: TextView
    private lateinit var watchingTextView: TextView
    private lateinit var watchedTextView: TextView
    private lateinit var favouritesTextView: TextView
    private lateinit var avgRatingTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        toolbar = findViewById(R.id.statsToolbar)
        toolbar.setNavigationOnClickListener { finish() }

        summaryTextView = findViewById(R.id.summaryTextView)
        summaryDetailTextView = findViewById(R.id.summaryDetailTextView)
        planTextView = findViewById(R.id.planTextView)
        watchingTextView = findViewById(R.id.watchingTextView)
        watchedTextView = findViewById(R.id.watchedTextView)
        favouritesTextView = findViewById(R.id.favouritesTextView)
        avgRatingTextView = findViewById(R.id.avgRatingTextView)

        viewModel.allMovies.observe(this) { movies ->
            updateStats(movies)
        }
    }

    private fun updateStats(movies: List<MovieEntity>) {
        val total = movies.size
        val plan = movies.count { it.status == "Plan to watch" }
        val watching = movies.count { it.status == "Watching" }
        val watched = movies.count { it.status == "Watched" }
        val favourites = movies.count { it.isFavourite }

        val rated = movies.filter { it.personalRating != null }
        val avg = if (rated.isNotEmpty()) {
            rated.map { it.personalRating!! }.average()
        } else null

        // Summary card (year-in-review style)
        if (total == 0) {
            summaryTextView.text = "You haven't logged any movies yet"
            summaryDetailTextView.text = "Start adding movies to build your story."
        } else {
            summaryTextView.text = "So far, you've logged $total movie(s)"
            summaryDetailTextView.text =
                "That's $watched watched, $watching in progress, and $plan still on your watchlist."
        }

        // Status breakdown card
        planTextView.text = "Plan to watch: $plan"
        watchingTextView.text = "Watching: $watching"
        watchedTextView.text = "Watched: $watched"
        favouritesTextView.text = "Favourites: $favourites"

        // Rating story card
        avgRatingTextView.text = if (avg != null) {
            "Average rating (rated only): %.1f / 10".format(avg)
        } else {
            "Average rating: N/A (no ratings yet)"
        }
    }
}
