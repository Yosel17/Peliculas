package cursos.aprender.peliculas.repository

import cursos.aprender.peliculas.core.InternetCheck
import cursos.aprender.peliculas.data.local.LocalMovieDataSource
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.data.model.MovieEntity
import cursos.aprender.peliculas.data.model.MovieList
import cursos.aprender.peliculas.data.model.toMovieEntity
import cursos.aprender.peliculas.data.remote.RemotoMovieDataSource

class MovieRepositoryImpl(
    private val dataSourceRemoto: RemotoMovieDataSource,
    private val dataSourceLocal: LocalMovieDataSource,
) : MovieRepository {

    override suspend fun getUpcomingMovies(): MovieList {
        return if (InternetCheck.isNetworkAvailable()){
            dataSourceRemoto.getUpcomingMovies().results.forEach { movie ->
                dataSourceLocal.saveMovie(movie.toMovieEntity())
            }
            dataSourceLocal.getUpcomingMovies()
        } else{

            dataSourceLocal.getUpcomingMovies()

        }

    }


    override suspend fun registerLikeButtonState(movie: Movie) = dataSourceLocal.registerLikeButtonState(movie.toMovieEntity())

    override suspend fun getFavorite(): MovieList {

        return dataSourceLocal.getFavorite()
    }

    override suspend fun registerLaterButtonState(movie: Movie) = dataSourceLocal.registerLaterButtonState(movie.toMovieEntity())

    override suspend fun getLater(): MovieList {
        return dataSourceLocal.getLater()
    }

}