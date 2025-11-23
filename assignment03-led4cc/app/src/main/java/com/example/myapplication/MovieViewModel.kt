package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.MovieEntity
import com.example.myapplication.data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    val allMovies: LiveData<List<MovieEntity>>

    init {
        val dao = AppDatabase.getInstance(application).movieDao()
        repository = MovieRepository(dao)
        allMovies = repository.getAllMovies()
    }

    fun addMovie(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(movie)
        }
    }

    fun updateMovie(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(movie)
        }
    }

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(movie)
        }
    }


}
