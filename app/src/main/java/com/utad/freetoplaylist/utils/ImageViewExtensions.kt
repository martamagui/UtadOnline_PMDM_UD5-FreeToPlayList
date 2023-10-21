package com.utad.freetoplaylist.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.utad.freetoplaylist.R

//Esta función extiende de ImageView,por lo que podremos usarla
//con cualquier imágen de nuestras vistas.
fun ImageView.loadPicture(url: String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.bg_placeholder)
        .into(this)
}