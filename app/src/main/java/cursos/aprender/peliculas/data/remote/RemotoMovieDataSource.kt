package cursos.aprender.peliculas.data.remote


import cursos.aprender.peliculas.application.AppConstants
import cursos.aprender.peliculas.data.model.MovieList
import cursos.aprender.peliculas.repository.WebService

class RemotoMovieDataSource(private val webService: WebService) {

    suspend fun getUpcomingMovies(): MovieList = webService.getUpcomingMovies(AppConstants.API_KEY)

}