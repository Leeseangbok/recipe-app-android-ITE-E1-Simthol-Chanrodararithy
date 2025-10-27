package com.example.recipefinderapp.ui.screens.explore

import androidx.lifecycle.SavedStateHandle
import android.util.Log
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

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedArea = MutableStateFlow("All")
    val selectedArea: StateFlow<String> = _selectedArea.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredMeals: StateFlow<List<Meal>> =
        combine(
            _allMeals,
            _selectedCategory,
            _selectedArea,
            _searchQuery
        ) { meals, category, area, query ->
            val categoryFiltered = if (category == "All") {
                meals
            } else {
                meals.filter { it.category == category }
            }

            val areaFiltered = if (area == "All") {
                categoryFiltered
            } else {
                categoryFiltered.filter { it.area == area }
            }

            if (query.isBlank()) {
                areaFiltered
            } else {
                areaFiltered.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        _selectedCategory.value = "All"
        _selectedArea.value = "All"
        _searchQuery.value = ""

        loadData()
    }
    private fun loadData() {
        viewModelScope.launch {
            Log.d("ExploreViewModel", "Loading data...")
            try {
                _allMeals.value = repository.getMeals()
                _categories.value = repository.getCategories()
                Log.d("ExploreViewModel", "Data loaded successfully.")
            } catch (e: Exception) {
                _allMeals.value = emptyList()
                _categories.value = emptyList()
                Log.e("ExploreViewModel", "Error loading data", e)
            }
        }
    }


    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onAreaSelected(area: String) {
        _selectedArea.value = area
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}