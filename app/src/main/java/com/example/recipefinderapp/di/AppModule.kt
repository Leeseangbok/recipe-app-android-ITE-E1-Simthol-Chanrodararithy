package com.example.recipefinderapp.di

import android.app.Application
import com.example.recipefinderapp.data.local.RecipeDatabase
import com.example.recipefinderapp.data.remote.RecipeApi
import com.example.recipefinderapp.data.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.room.Room


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://meal-db-sandy.vercel.app/"

    @Provides
    @Singleton
    fun provideRecipeApi(): RecipeApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApi::class.java)

    @Provides
    @Singleton
    fun provideRecipeDatabase(app: Application): RecipeDatabase =
        Room.databaseBuilder(
            app,
            RecipeDatabase::class.java,
            "recipe_db"
        ).build()

    @Provides
    @Singleton
    fun provideRecipeRepository(
        db: RecipeDatabase,
        api: RecipeApi
    ): RecipeRepository =
        RecipeRepository(api, db.dao)
}