package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.MovieEntity

class MovieAdapter(
    private val onItemClick: (MovieEntity) -> Unit
) : ListAdapter<MovieEntity, MovieAdapter.MovieViewHolder>(DiffCallback()) {

    class MovieViewHolder(itemView: View, val onItemClick: (MovieEntity) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView =
            itemView.findViewById(R.id.titleTextView)
        private val yearGenreTextView: TextView =
            itemView.findViewById(R.id.yearGenreTextView)
        private val statusTextView: TextView =
            itemView.findViewById(R.id.statusTextView)
        private val ratingTextView: TextView =
            itemView.findViewById(R.id.ratingTextView)
        private val flagsTextView: TextView =
            itemView.findViewById(R.id.flagsTextView)

        private var currentMovie: MovieEntity? = null

        init {
            itemView.setOnClickListener {
                currentMovie?.let { onItemClick(it) }
            }
        }

        fun bind(movie: MovieEntity) {
            currentMovie = movie
            titleTextView.text = movie.title

            val yearGenre = buildString {
                movie.year?.let { append(it.toString()) }
                if (movie.year != null && movie.genre.isNotBlank()) append(" • ")
                if (movie.genre.isNotBlank()) append(movie.genre)
            }
            yearGenreTextView.text = yearGenre

            statusTextView.text = "Status: ${movie.status}"

            ratingTextView.text = movie.personalRating?.let {
                "★ ${String.format("%.1f", it)}"
            } ?: "Not rated"

            val flags = mutableListOf<String>()
            if (movie.isFavourite) flags.add("FAV")
            if (movie.isBuster && movie.status != "Watched") flags.add("BUSTER")
            flagsTextView.text = flags.joinToString(" • ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_movie, parent, false)
        return MovieViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            oldItem == newItem
    }
}
