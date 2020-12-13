package com.infinity.movieapp.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.infinity.movieapp.BR
import com.infinity.movieapp.R
import com.infinity.movieapp.databinding.FragmentMovieDetailBinding
import com.infinity.movieapp.ui.MainActivity
import com.infinity.movieapp.ui.MovieViewModel

class MovieDetailFragment : Fragment() {


    val args: MovieDetailFragmentArgs by navArgs()
    lateinit var viewModel : MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentMovieDetailBinding>(inflater, R.layout.fragment_movie_detail , container ,false)

        viewModel = (activity as  MainActivity).viewModel

        binding.apply {
            binding.setVariable(BR.movie,args.movie)
        }
        binding.fab.setOnClickListener {
            viewModel.saveArticle(args.movie)
            Snackbar.make(requireView(), "Article Saved Successfully", Snackbar.LENGTH_SHORT).show()
        }

        return  binding.root
    }


}