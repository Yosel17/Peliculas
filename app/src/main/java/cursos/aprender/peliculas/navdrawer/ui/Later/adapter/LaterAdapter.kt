package cursos.aprender.peliculas.navdrawer.ui.Later.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cursos.aprender.peliculas.core.BaseViewHolder
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.databinding.FavoriteItemBinding
import cursos.aprender.peliculas.databinding.LaterItemBinding
import cursos.aprender.peliculas.navdrawer.ui.Later.LaterFragment
import cursos.aprender.peliculas.navdrawer.ui.favorite.FavoriteFragment
import cursos.aprender.peliculas.navdrawer.ui.favorite.adapter.FavoriteAdapter

class LaterAdapter(
    private val laterList: List<Movie>,
    private val itemClickListener: LaterFragment,
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            LaterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = LaterViewHolder(itemBinding, parent.context)
        itemBinding.root.setOnClickListener() {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            itemClickListener.onMovieClick(laterList[position])

        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is LaterViewHolder -> holder.bind(laterList[position])
        }
    }

    override fun getItemCount(): Int = laterList.size

    private inner class LaterViewHolder(val binding: LaterItemBinding, val context: Context) :
        BaseViewHolder<Movie>(binding.root) {
        override fun bind(item: Movie) {
            Glide.with(context).load("https://image.tmdb.org/t/p/w500/${item.poster_path}")
                .centerCrop().into(binding.imgMovie)
            binding.tvName.text = item.original_title
            binding.tvRating.text = "${item.vote_average} (${item.vote_count} reviews)"

        }

    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)



    }
}