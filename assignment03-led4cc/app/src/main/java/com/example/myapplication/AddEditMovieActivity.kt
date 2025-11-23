package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.example.myapplication.data.MovieEntity

class AddEditMovieActivity : ComponentActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }

    private val viewModel: MovieViewModel by viewModels()

    private lateinit var toolbar: MaterialToolbar
    private lateinit var titleEditText: EditText
    private lateinit var yearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var ratingEditText: EditText
    private lateinit var favouriteCheckBox: CheckBox
    private lateinit var busterCheckBox: CheckBox
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var deleteButton: Button

    private var editingMovieId: Int = 0
    private var loadedMovie: MovieEntity? = null
    private var filledFromExisting = false

    private val statusOptions = listOf("Plan to watch", "Watching", "Watched")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_movie)

        editingMovieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0)

        setupViews()
        setupStatusSpinner()
        setupToolbar()

        if (editingMovieId != 0) {
            toolbar.title = "Edit movie"
            deleteButton.visibility = View.VISIBLE
            observeMovieForEdit()
        } else {
            toolbar.title = "Add movie"
        }

        setupButtons()
    }

    private fun setupViews() {
        toolbar = findViewById(R.id.addEditToolbar)
        titleEditText = findViewById(R.id.titleEditText)
        yearEditText = findViewById(R.id.yearEditText)
        genreEditText = findViewById(R.id.genreEditText)
        statusSpinner = findViewById(R.id.statusSpinner)
        ratingEditText = findViewById(R.id.ratingEditText)
        favouriteCheckBox = findViewById(R.id.favouriteCheckBox)
        busterCheckBox = findViewById(R.id.busterCheckBox)
        notesEditText = findViewById(R.id.notesEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        deleteButton = findViewById(R.id.deleteButton)
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupStatusSpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner_text,
            statusOptions
        ).also {
            it.setDropDownViewResource(R.layout.item_spinner_dropdown_text)
        }
        statusSpinner.adapter = adapter
    }

    private fun observeMovieForEdit() {
        // Reuse the existing LiveData list and find the movie by ID once
        viewModel.allMovies.observe(this) { movies ->
            if (!filledFromExisting) {
                val movie = movies.firstOrNull { it.id == editingMovieId }
                if (movie != null) {
                    loadedMovie = movie
                    fillFieldsFromMovie(movie)
                    filledFromExisting = true
                }
            }
        }
    }

    private fun fillFieldsFromMovie(movie: MovieEntity) {
        titleEditText.setText(movie.title)
        movie.year?.let { yearEditText.setText(it.toString()) }
        genreEditText.setText(movie.genre)
        val statusIndex = statusOptions.indexOf(movie.status).takeIf { it >= 0 } ?: 0
        statusSpinner.setSelection(statusIndex)
        movie.personalRating?.let { ratingEditText.setText(it.toString()) }
        favouriteCheckBox.isChecked = movie.isFavourite
        busterCheckBox.isChecked = movie.isBuster
        notesEditText.setText(movie.notes ?: "")
    }

    private fun setupButtons() {
        cancelButton.setOnClickListener { finish() }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().ifBlank { "Untitled" }
            val year = yearEditText.text.toString().toIntOrNull()
            val genre = genreEditText.text.toString()
            val status = statusOptions[statusSpinner.selectedItemPosition]
            val rating = ratingEditText.text.toString().toFloatOrNull()
            val isFavourite = favouriteCheckBox.isChecked
            val isBuster = busterCheckBox.isChecked
            val notes = notesEditText.text.toString().ifBlank { null }

            val movieToSave = MovieEntity(
                id = editingMovieId, // 0 for new, existing id for edit
                title = title,
                year = year,
                genre = genre,
                status = status,
                personalRating = rating,
                isFavourite = isFavourite,
                dateWatched = null,
                notes = notes,
                isBuster = isBuster
            )

            if (editingMovieId == 0) {
                viewModel.addMovie(movieToSave)
            } else {
                viewModel.updateMovie(movieToSave)
            }
            finish()
        }

        deleteButton.setOnClickListener {
            loadedMovie?.let { movie ->
                viewModel.deleteMovie(movie)
            }
            finish()
        }
    }
}
