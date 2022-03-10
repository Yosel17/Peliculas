package cursos.aprender.peliculas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cursos.aprender.peliculas.core.Resource
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.data.model.MovieEntity
import cursos.aprender.peliculas.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest


class MovieViewModel(private val repo: MovieRepository): ViewModel() {


    fun fetchMainScreenMovies() = liveData(Dispatchers.IO){
        emit(Resource.Loading())

        try {
            emit(Resource.Success((repo.getUpcomingMovies())))

        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun registerLikeButtonState(movie: Movie) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Resource.Loading())
        kotlin.runCatching {
            repo.registerLikeButtonState(movie)
        }.onSuccess {
            emit(Resource.Success(repo.registerLikeButtonState(movie)))
        }.onFailure {
            emit(Resource.Failure(Exception(it.message)))
        }
    }

    fun registerLaterButtonState(movie: Movie) = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Resource.Loading())
        kotlin.runCatching {
            repo.registerLaterButtonState(movie)
        }.onSuccess {
            emit(Resource.Success(repo.registerLaterButtonState(movie)))
        }.onFailure {
            emit(Resource.Failure(Exception(it.message)))
        }
    }

    fun fetchMainScreenFavorite() = liveData(Dispatchers.IO){
        emit(Resource.Loading())

        try {
            emit(Resource.Success((repo.getFavorite())))

        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun fetchMainScreenLater() = liveData(Dispatchers.IO){
        emit(Resource.Loading())

        try {
            emit(Resource.Success((repo.getLater())))

        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}

class MovieViewModelFactoru(private val repo: MovieRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MovieRepository::class.java).newInstance(repo)
    }
}