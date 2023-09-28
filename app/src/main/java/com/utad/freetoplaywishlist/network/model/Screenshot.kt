package com.utad.freetoplaywishlist.network.model

import com.google.gson.annotations.SerializedName

data class Screenshot(
    @SerializedName("id")
    val id : Int,
    @SerializedName("image")
    val image : String
)