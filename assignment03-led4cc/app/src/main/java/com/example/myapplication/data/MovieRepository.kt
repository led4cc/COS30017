package com.example.myapplication.data

import androidx.lifecycle.LiveData

class MovieRepository(private val movieDao: MovieDao) {

    fun getAllMovies(): LiveData<List<MovieEntity>> = movieDao.getAllMovies()

    fun getMoviesByStatus(status: String): LiveData<List<MovieEntity>> =
        movieDao.getMoviesByStatus(status)

    fun getFavouriteMovies(): LiveData<List<MovieEntity>> =
        movieDao.getFavouriteMovies()

    suspend fun insert(movie: MovieEntity) = movieDao.insert(movie)

    suspend fun update(movie: MovieEntity) = movieDao.update(movie)

    suspend fun delete(movie: MovieEntity) = movieDao.delete(movie)
}
