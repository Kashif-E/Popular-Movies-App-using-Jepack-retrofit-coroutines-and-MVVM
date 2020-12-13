package com.infinity.movieapp.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.infinity.movieapp.R
import com.infinity.movieapp.adapter.MoviesAdapter
import com.infinity.movieapp.databinding.FragmentTopratedMoviesBinding
import com.infinity.movieapp.ui.MainActivity
import com.infinity.movieapp.ui.MovieViewModel
import com.infinity.movieapp.util.Resource


class TopRatedFragment : Fragment() {



    lateinit var  moviesAdapter: MoviesAdapter
    lateinit var  binding : FragmentTopratedMoviesBinding
    lateinit var  viewModel : MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_toprated_movies, container, false)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("movie",it)
            }
            findNavController().navigate(R.id.action_latestMoviesFragment_to_movieDetailFragment, bundle)
        }



        viewModel.latestMovies.observe(viewLifecycleOwner, Observer { response ->


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

        return binding.root
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