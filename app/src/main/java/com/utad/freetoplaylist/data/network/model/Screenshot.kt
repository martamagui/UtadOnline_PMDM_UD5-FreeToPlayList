package com.utad.freetoplaylist.data.network.model

import com.google.gson.annotations.SerializedName

data class Screenshot(
    @SerializedName("id")
    val id : Int,
    @SerializedName("image")
    val image : String
)