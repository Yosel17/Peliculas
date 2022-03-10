package cursos.aprender.peliculas.navdrawer.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import cursos.aprender.peliculas.R
import cursos.aprender.peliculas.core.Resource
import cursos.aprender.peliculas.data.local.AppDatabase
import cursos.aprender.peliculas.data.local.LocalMovieDataSource
import cursos.aprender.peliculas.data.model.Movie
import cursos.aprender.peliculas.data.model.MovieEntity
import cursos.aprender.peliculas.data.remote.RemotoMovieDataSource
import cursos.aprender.peliculas.databinding.FragmentHomeBinding
import cursos.aprender.peliculas.presentation.MovieViewModel
import cursos.aprender.peliculas.presentation.MovieViewModelFactoru
import cursos.aprender.peliculas.repository.MovieRepositoryImpl
import cursos.aprender.peliculas.repository.RetrofitClient
import cursos.aprender.peliculas.ui.movie.adapters.MovieAdapter
import cursos.aprender.peliculas.util.onQueryTextChanged


class HomeFragment : Fragment(), MovieAdapter.OnMovieClickListener {

    private var _binding: FragmentHomeBinding? = null

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        homeViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (resources.getBoolean(R.bool.isTablet)){
                        binding.rvMovies.layoutManager = GridLayoutManager(context, 4)
                    } else{
                        binding.rvMovies.layoutManager = GridLayoutManager(context, 2)
                    }
                    binding.rvMovies.adapter = MovieAdapter(it.data.results, this)


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
        val action = HomeFragmentDirections.actionNavHomeToMovieDetailFragment(
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

    override fun onLikeButtonClick(movie: Movie) {
        viewModel.registerLikeButtonState(movie).observe(viewLifecycleOwner){result ->
            when(result){
                is Resource.Loading -> {}
                is Resource.Success -> {Unit}
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onLaterButtonClick(movie: Movie) {
        viewModel.registerLaterButtonState(movie).observe(viewLifecycleOwner){result ->
            when(result){
                is Resource.Loading -> {}
                is Resource.Success -> {Unit}
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}