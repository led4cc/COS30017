package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.example.myapplication.data.MovieEntity

class BustersActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var toolbar: MaterialToolbar
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    private var allMovies: List<MovieEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busters)

        toolbar = findViewById(R.id.bustersToolbar)
        emptyTextView = findViewById(R.id.bustersEmptyTextView)
        recyclerView = findViewById(R.id.bustersRecyclerView)

        toolbar.setNavigationOnClickListener { finish() }

        adapter = MovieAdapter { movie ->
            // open AddEditMovieActivity in edit mode
            val intent = Intent(this, AddEditMovieActivity::class.java)
            intent.putExtra(AddEditMovieActivity.EXTRA_MOVIE_ID, movie.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.allMovies.observe(this) { movies ->
            allMovies = movies
            applyFilter()
        }
    }

    private fun applyFilter() {
        val busters = allMovies.filter { it.isBuster && it.status != "Watched" }
        if (busters.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
        adapter.submitList(busters)
    }
}
