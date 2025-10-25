package com.example.recipefinderapp.data.remote


import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RecipeApi{

    @Headers("X-DB-NAME: 57a5c413-9c11-4aab-b80e-0ce3d7df7129")
    @GET("meals")
    suspend fun getMeals(): List<Meal>

    @Headers("X-DB-NAME: 57a5c413-9c11-4aab-b80e-0ce3d7df7129")
    @GET("meals/{id}")
    suspend fun getMealById(@Path("id") mealId: String): Meal

    @Headers("X-DB-NAME: 57a5c413-9c11-4aab-b80e-0ce3d7df7129")
    @GET("categories")
    suspend fun getCategories(): List<Category>

}