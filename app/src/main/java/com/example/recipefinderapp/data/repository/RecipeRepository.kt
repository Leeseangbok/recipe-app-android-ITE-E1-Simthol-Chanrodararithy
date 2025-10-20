package com.example.recipefinderapp.data.repository

import com.example.recipefinderapp.data.local.RecipeDao
import com.example.recipefinderapp.data.local.RecipeEntity
import com.example.recipefinderapp.data.remote.RecipeApi
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val api: RecipeApi,
    private val dao: RecipeDao
) {
    suspend fun getMeals() = api.getMeals()
    suspend fun getCategories() = api.getCategories()

    fun getFavorites(): Flow<List<RecipeEntity>> = dao.getAllFavorites()
    suspend fun addFavorite(recipe: RecipeEntity) = dao.insertFavorite(recipe)
    suspend fun removeFavoriteById(id: String) = dao.deleteFavoriteById(id)
}
