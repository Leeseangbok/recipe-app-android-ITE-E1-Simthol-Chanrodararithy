package com.example.recipefinderapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.local.RecipeEntity
import com.example.recipefinderapp.data.repository.RecipeRepository
import com.example.recipefinderapp.domain.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailViewModel @Inject constructor(
    private val repository: RecipeRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val isFavorite: StateFlow<Boolean> = meal.combine(repository.getFavorites()) { meal, favorites ->
        favorites.any { it.id == meal?.id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        savedStateHandle.get<String>("mealId")?.let { mealId ->
            if (mealId.isNotBlank()) {
                loadMeal(mealId)
            }
        }
    }

    private fun loadMeal(mealId: String){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                _meal.value = repository.getMealById(mealId)
            } catch (e: Exception){
                _meal.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentMeal = _meal.value ?: return@launch
            val currentlyIsFavorite = isFavorite.value

            if (currentlyIsFavorite) {
                repository.removeFavoriteById(currentMeal.id)
            } else {
                val recipeEntity = RecipeEntity(
                    id = currentMeal.id,
                    name = currentMeal.name,
                    imageUrl = currentMeal.image ?: ""
                )
                repository.addFavorite(recipeEntity)
            }
        }
    }
}