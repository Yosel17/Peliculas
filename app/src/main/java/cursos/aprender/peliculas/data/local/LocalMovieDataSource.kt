package cursos.aprender.peliculas.data.local

import cursos.aprender.peliculas.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class LocalMovieDataSource(private val movieDao: MovieDao) {
    //Todas las peliculas
    suspend fun getUpcomingMovies(): MovieList {
        return movieDao.getAllMovies().toMovieList()
        //return movieDao.getAllMovies().filter { it.favorite == true }.toMovieList()
    }
    suspend fun saveMovie(movie: MovieEntity){
        movieDao.saveMovie(movie)
    }

    suspend fun registerLikeButtonState(movie: MovieEntity) {
        movieDao.updateLike(movie)
    }

    suspend fun getFavorite(): MovieList{
        return movieDao.getFavorite().filter { it.favorite == 2 }.toMovieList()
    }

    suspend fun registerLaterButtonState(movie: MovieEntity){
        movieDao.updateLater(movie)
    }

    suspend fun getLater(): MovieList{
        return movieDao.getLater().filter { it.later == 2 }.toMovieList()
    }



}