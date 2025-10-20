package com.example.recipefinderapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String
)