package com.example.recipefinderapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.repository.RecipeRepository
import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _meals = MutableStateFlow(emptyList<Meal>())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _randomMeal = MutableStateFlow<Meal?>(null)
    val randomMeal: StateFlow<Meal?> = _randomMeal.asStateFlow()

    private val _categories = MutableStateFlow(emptyList<Category>())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    val areas: StateFlow<List<String>> = _meals.map { meals ->
        meals.mapNotNull { it.area }.distinct().sorted()
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val allMeals = repository.getMeals()
                val allCategories = repository.getCategories()
                _meals.value = allMeals
                _categories.value = allCategories
                if (_randomMeal.value == null) {
                    _randomMeal.value = allMeals.randomOrNull()
                }
            } catch (e: Exception) {
                _meals.value = emptyList()
                _categories.value = emptyList()
                _randomMeal.value = null
            }
        }
    }
}