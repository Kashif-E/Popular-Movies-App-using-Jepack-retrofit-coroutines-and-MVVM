package com.infinity.movieapp.extensions

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.hide(){
    this.visibility= View.INVISIBLE
}
fun ProgressBar.show(){
    this.visibility= View.VISIBLE
}