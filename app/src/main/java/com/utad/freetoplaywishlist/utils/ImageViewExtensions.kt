package com.utad.freetoplaywishlist.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.utad.freetoplaywishlist.R

fun ImageView.loadPicture(url: String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.bg_placeholder)
        .into(this)
}