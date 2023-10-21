package com.utad.freetoplaylist.network.model

import com.google.gson.annotations.SerializedName

data class Screenshot(
    @SerializedName("id")
    val id : Int,
    @SerializedName("image")
    val image : String
)