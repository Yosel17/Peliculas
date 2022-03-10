package cursos.aprender.peliculas.data.local

import androidx.room.*
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.data.model.MovieEntity

@Dao
interface MovieDao {
    //Room para todas las peliculas
    @Query("SELECT * FROM movieentity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLike(movie: MovieEntity)

    @Query("SELECT * FROM MOVIEENTITY")
    suspend fun getFavorite(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLater(movie: MovieEntity)

    @Query("SELECT * FROM MOVIEENTITY")
    suspend fun getLater(): List<MovieEntity>


}