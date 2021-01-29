package com.infinity.movieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.infinity.movieapp.R
import com.infinity.movieapp.adapter.MoviesAdapter
import com.infinity.movieapp.databinding.FragmentMovieBinding
import com.infinity.movieapp.ui.MovieViewModel
import com.infinity.movieapp.util.Resource

class PopularMoviesFragment : Fragment(R.layout.fragment_movie) {


    private val viewModel : MovieViewModel by  activityViewModels()
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var binding: FragmentMovieBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        setUpRecyclerView()

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("movie",it)
            }
            findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment, bundle)
        }

        viewModel.popularMovies.observe(viewLifecycleOwner,  { response ->


            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    moviesAdapter.differ.submitList(response.data!!.results)
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    response.message.let { message ->
                        hideProgressBar()
                        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        })
    }

    private fun setUpRecyclerView() {

        moviesAdapter = MoviesAdapter()
        binding.recyclerView.adapter = moviesAdapter

    }

    private fun showProgressBar() {

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }


}
