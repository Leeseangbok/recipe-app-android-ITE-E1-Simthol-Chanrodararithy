package com.example.recipefinderapp.ui.screens.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.repository.RecipeRepository
import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    val areas: StateFlow<List<String>> = _allMeals.map { meals ->
        meals.mapNotNull { it.area }.distinct().sorted()
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())
    private val _selectedCategory = MutableStateFlow<String>("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedArea = MutableStateFlow<String>("All")
    val selectedArea: StateFlow<String> = _selectedArea.asStateFlow()

    val filteredMeals: StateFlow<List<Meal>> =
        combine(_allMeals, _selectedCategory, _selectedArea) { meals, category, area ->

            val categoryFiltered = if (category == "All") {
                meals
            } else {
                meals.filter { it.category == category }
            }

            if (area == "All") {
                categoryFiltered
            } else {
                categoryFiltered.filter { it.area == area }
            }
        }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        val initialCategory = savedStateHandle.get<String>("category")
        val initialArea = savedStateHandle.get<String>("area")

        if (initialCategory != null) {
            _selectedCategory.value = initialCategory
        } else {
            _selectedCategory.value = "All"
        }
        if (initialArea != null) {
            _selectedArea.value = initialArea
        }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _allMeals.value = repository.getMeals()
                _categories.value = repository.getCategories()
            } catch (e: Exception) {
                _allMeals.value = emptyList()
                _categories.value = emptyList()
            }
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onAreaSelected(area: String) {
        _selectedArea.value = area
    }
}