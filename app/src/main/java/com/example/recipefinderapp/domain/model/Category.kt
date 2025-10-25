package com.example.recipefinderapp.domain.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("category") val name: String,
    @SerializedName("categoryDescription") val description: String? = null,
    @SerializedName("categoryThumb") val image: String? = null
)