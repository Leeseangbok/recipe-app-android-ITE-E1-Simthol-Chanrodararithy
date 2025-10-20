package com.example.recipefinderapp.domain.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String? = null,
    @SerializedName("area") val area: String? = null,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("tags") val tags: String? = null,
    @SerializedName("youtube") val youtube: String? = null,
    @SerializedName("ingredients") val ingredients: List<String>? = null,
    @SerializedName("measures") val measures: List<String>? = null
)