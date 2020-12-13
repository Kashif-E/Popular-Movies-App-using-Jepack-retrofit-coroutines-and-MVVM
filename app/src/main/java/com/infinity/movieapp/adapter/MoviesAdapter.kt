package com.infinity.movieapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.movieapp.R
import com.infinity.movieapp.databinding.MovieCardBinding
import com.infinity.movieapp.models.Result
import com.infinity.movieapp.ui.Fragments.MovieFragmentDirections


class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> (){





    inner class  MoviesViewHolder(val itemViewBinding: MovieCardBinding): RecyclerView.ViewHolder(
        itemViewBinding.root
    ) {

        fun bindView(movieItem: Result) {
            itemViewBinding.apply {
                movie = movieItem
            }

            itemViewBinding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(movieItem)

                }
            }
        }
    }

    private val differCallBack  = object : DiffUtil.ItemCallback<Result>()
    {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
          return  oldItem.id== newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return  oldItem==newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(

            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.movie_card, parent, false
            )
        )
    }

    private var onItemClickListener: ((Result) -> Unit)? = null
    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {

        val movieItem= differ.currentList[position]
      holder.bindView(movieItem)
        }




    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener

    }
}