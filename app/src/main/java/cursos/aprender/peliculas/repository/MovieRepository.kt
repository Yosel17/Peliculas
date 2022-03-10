package cursos.aprender.peliculas.repository

import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.data.model.MovieEntity
import cursos.aprender.peliculas.data.model.MovieList

interface MovieRepository {
    suspend fun getUpcomingMovies(): MovieList
    suspend fun registerLikeButtonState(movie: Movie)
    suspend fun getFavorite(): MovieList
    suspend fun registerLaterButtonState(movie: Movie)
    suspend fun getLater(): MovieList



}