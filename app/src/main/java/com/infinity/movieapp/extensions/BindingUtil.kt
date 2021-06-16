package com.infinity.movieapp.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


@BindingAdapter("poster")
fun loadPoster(view: ImageView, url: String) {
    Glide.with(view).load("https://image.tmdb.org/t/p/original/$url").diskCacheStrategy(
        DiskCacheStrategy.ALL).into(view)
}

