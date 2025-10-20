package com.example.recipefinderapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.repository.RecipeRepository
import com.example.recipefinderapp.domain.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _meals = MutableStateFlow(emptyList<com.example.recipefinderapp.domain.model.Meal>())
    val meals: StateFlow<List<Meal>> = _meals

    fun loadMeals() {
        viewModelScope.launch {
            _meals.value = repository.getMeals()
        }
    }
}