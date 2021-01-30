package com.infinity.movieapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.infinity.movieapp.R
import com.infinity.movieapp.adapter.MoviesAdapter
import com.infinity.movieapp.databinding.FragmentTopratedMoviesBinding
import com.infinity.movieapp.extensions.hide
import com.infinity.movieapp.extensions.show
import com.infinity.movieapp.extensions.toast
import com.infinity.movieapp.ui.MovieViewModel
import com.infinity.movieapp.util.Resource


class TopRatedFragment : Fragment(R.layout.fragment_toprated_movies) {



    lateinit var  moviesAdapter: MoviesAdapter
    lateinit var  binding : FragmentTopratedMoviesBinding
    private val viewModel: MovieViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTopratedMoviesBinding.bind(view)
        setUpRecyclerView()

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("movie",it)
            }
            findNavController().navigate(R.id.action_latestMoviesFragment_to_movieDetailFragment, bundle)
        }



        viewModel.latestMovies.observe(viewLifecycleOwner, { response ->


            when (response) {
                is Resource.Success -> {
                    binding.progressBar.hide()
                    moviesAdapter.differ.submitList(response.data!!.results)
                }
                is Resource.Loading -> {
                    binding.progressBar.show()
                }
                is Resource.Error -> {
                    response.message.let { message ->
                        binding.progressBar.hide()
                       context?.toast(message.toString())
                    }
                }
            }


        })

    }

    private fun setUpRecyclerView() {

        moviesAdapter = MoviesAdapter()
        binding.recyclerView.adapter = moviesAdapter

    }



}