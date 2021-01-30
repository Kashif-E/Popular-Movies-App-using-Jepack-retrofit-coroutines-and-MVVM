package com.infinity.movieapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.infinity.movieapp.R
import com.infinity.movieapp.adapter.MoviesAdapter
import com.infinity.movieapp.databinding.FragmentSavedMoviesBinding
import com.infinity.movieapp.ui.MovieViewModel


class SavedMoviesFragment : Fragment(R.layout.fragment_saved_movies) {


    lateinit var binding: FragmentSavedMoviesBinding
    lateinit var moviesAdapter: MoviesAdapter
    private val viewModel: MovieViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedMoviesBinding.bind(view)
        setupRecycleView()

        viewModel.getSavedMovies().observe(viewLifecycleOwner, { movies ->
            moviesAdapter.differ.submitList(movies)
        })

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("movie",it)
            }

            findNavController().navigate(R.id.action_savedMoviesFragment_to_movieDetailFragment,bundle)

        }

        setupSwipeToDeleteFunction()


    }

    private fun setupSwipeToDeleteFunction() {
        val itemTouchHelperCallBack =object : ItemTouchHelper.SimpleCallback (
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        )
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = moviesAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)

                Snackbar.make(requireView(),"Successfully deleted ", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                }.show()
            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun setupRecycleView() {
        moviesAdapter = MoviesAdapter()
        binding.recyclerView.adapter = moviesAdapter
    }


}