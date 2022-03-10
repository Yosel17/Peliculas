package cursos.aprender.peliculas.ui.movie.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cursos.aprender.peliculas.R
import cursos.aprender.peliculas.core.BaseViewHolder
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.databinding.MovieItemBinding

class MovieAdapter(
    private val moviesList: List<Movie>,
    private val itemClickListener: OnMovieClickListener,
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var movieClickListener: OnMovieClickListener? = null
    private var laterClickListener: OnMovieClickListener? = null

    init {
        movieClickListener = itemClickListener
        laterClickListener = itemClickListener
    }


    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
        fun onLikeButtonClick(movie: Movie)
        fun onLaterButtonClick(movie: Movie)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = MoviesViewHolder(itemBinding, parent.context)

        itemBinding.root.setOnClickListener() {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            itemClickListener.onMovieClick(moviesList[position])

        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MoviesViewHolder -> holder.bind(moviesList[position])
            //is MoviesViewHolder2 -> holder.bind(moviesList[position].toMovieEntity())
        }
    }

    override fun getItemCount(): Int = moviesList.size

    private inner class MoviesViewHolder(val binding: MovieItemBinding, val context: Context) :
        BaseViewHolder<Movie>(binding.root) {
        override fun bind(item: Movie) {
            Glide.with(context).load("https://image.tmdb.org/t/p/w500/${item.poster_path}")
                .centerCrop().into(binding.imgMovie)
            binding.tvName.text = item.original_title
            binding.tvRating.text = "${item.vote_average} (${item.vote_count} reviews)"

            tintHeartIcon(item)
            setLikeClickAction(item)
            //laterMovie
            tintLaterIcon(item)
            setLaterClickAction(item)
        }




        private fun tintHeartIcon(movie: Movie) {
            if (movie.favorite == 1){
                binding.ibLiked.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_corazon))
            } else{
                binding.ibLiked.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_corazon_lleno))
            }
        }

        private fun setLikeClickAction(movie: Movie) {
            binding.ibLiked.setOnClickListener {
                //if (movie.favorite == 1) movie.apply { favorite = 2 } else movie.apply { favorite = 1 }
                if (movie.favorite == 1){
                    movie.apply { favorite = 2 }
                } else if (movie.favorite == 2){
                    movie.apply { favorite = 1 }
                }
                tintHeartIcon(movie)
                movieClickListener?.onLikeButtonClick(movie)


            }
        }

        private fun setLaterClickAction(movie: Movie) {
            binding.ibSave.setOnClickListener {
                if (movie.later == 1){
                    movie.apply { later = 2 }
                } else if (movie.later == 2){
                    movie.apply { later = 1 }
                }
                tintLaterIcon(movie)
                laterClickListener?.onLaterButtonClick(movie)

            }
        }

        private fun tintLaterIcon(movie: Movie) {
            if (movie.later == 1){
                binding.ibSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_guardar))
            } else{
                binding.ibSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_guardar_lleno))

            }
        }

    }
}

