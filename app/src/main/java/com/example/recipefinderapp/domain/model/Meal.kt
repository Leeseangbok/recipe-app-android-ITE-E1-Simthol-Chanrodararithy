package com.example.recipefinderapp.domain.model

import com.google.gson.annotations.SerializedName

// --- ADD THIS NEW DATA CLASS ---
data class IngredientMeasure(
    @SerializedName("ingredient") val ingredient: String?,
    @SerializedName("measure") val measure: String?
)

data class Meal(
    @SerializedName("id") val id: String,
    @SerializedName("meal") val name: String,
    @SerializedName("category") val category: String? = null,
    @SerializedName("area") val area: String? = null,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("mealThumb") val image: String? = null,
    @SerializedName("tags") val tags: String? = null,
    @SerializedName("youtube") val youtube: String? = null,
    @SerializedName("ingredients") val ingredients: List<IngredientMeasure>? = null
)