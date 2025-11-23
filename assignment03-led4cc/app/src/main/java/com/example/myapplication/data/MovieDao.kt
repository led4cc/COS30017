package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY title COLLATE NOCASE ASC")
    fun getAllMovies(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE status = :status ORDER BY title COLLATE NOCASE ASC")
    fun getMoviesByStatus(status: String): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isFavourite = 1 ORDER BY title COLLATE NOCASE ASC")
    fun getFavouriteMovies(): LiveData<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Update
    suspend fun update(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)
}
