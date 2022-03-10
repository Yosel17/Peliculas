package cursos.aprender.peliculas.navdrawer.ui.Later

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cursos.aprender.peliculas.R
import cursos.aprender.peliculas.core.Resource
import cursos.aprender.peliculas.data.local.AppDatabase
import cursos.aprender.peliculas.data.local.LocalMovieDataSource
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.data.remote.RemotoMovieDataSource
import cursos.aprender.peliculas.databinding.FragmentLaterBinding
import cursos.aprender.peliculas.navdrawer.ui.Later.adapter.LaterAdapter
import cursos.aprender.peliculas.navdrawer.ui.favorite.FavoriteFragmentDirections
import cursos.aprender.peliculas.navdrawer.ui.favorite.adapter.FavoriteAdapter
import cursos.aprender.peliculas.presentation.MovieViewModel
import cursos.aprender.peliculas.presentation.MovieViewModelFactoru
import cursos.aprender.peliculas.repository.MovieRepositoryImpl
import cursos.aprender.peliculas.repository.RetrofitClient


class LaterFragment : Fragment(), LaterAdapter.OnMovieClickListener {

    private var _binding: FragmentLaterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactoru(
            MovieRepositoryImpl(
                RemotoMovieDataSource(RetrofitClient.webservice),
                LocalMovieDataSource(AppDatabase.getDatabase(requireContext()).movieDao()),
            )
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(LaterViewModel::class.java)

        _binding = FragmentLaterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchMainScreenLater().observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (resources.getBoolean(R.bool.isTablet)){
                        binding.rvLater.layoutManager = GridLayoutManager(context, 4)
                    } else{
                        binding.rvLater.layoutManager = GridLayoutManager(context, 2)
                    }
                    binding.rvLater.adapter = LaterAdapter(it.data.results, this)


                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE

                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movie: Movie) {
        val action = LaterFragmentDirections.actionNavGalleryToMovieDetailFragment2(
            movie.poster_path,
            movie.backdrop_paht,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.original_language,
            movie.release_date
        )
        findNavController().navigate(action)
    }
}