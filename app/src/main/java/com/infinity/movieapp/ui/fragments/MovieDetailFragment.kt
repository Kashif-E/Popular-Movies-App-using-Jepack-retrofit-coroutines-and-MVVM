package com.infinity.movieapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.infinity.movieapp.R
import com.infinity.movieapp.databinding.FragmentMovieDetailBinding
import com.infinity.movieapp.models.domainmodel.asDataBaseModel
import com.infinity.movieapp.ui.MovieViewModel

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {


    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel : MovieViewModel by activityViewModels()
    lateinit var binding: FragmentMovieDetailBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMovieDetailBinding.bind(view)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.movie = args.movie

        binding.fab.setOnClickListener {
         viewModel.saveMovie(args.movie.asDataBaseModel())
            Snackbar.make(requireView(), "Movie Saved Successfully", Snackbar.LENGTH_SHORT).show()
        }
    }


}