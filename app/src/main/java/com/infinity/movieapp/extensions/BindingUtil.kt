package com.infinity.movieapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


    @BindingAdapter("poster")
    fun loadPoster(view : ImageView, url : String) {
        Glide.with(view).load("https://image.tmdb.org/t/p/original/$url").into(view)
    }
