@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipefinderapp.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.ui.navigation.Screen

@Composable
fun ExploreScreen(
    navController: NavHostController,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val areas by viewModel.areas.collectAsState()
    val selectedArea by viewModel.selectedArea.collectAsState()

    val filteredMeals by viewModel.filteredMeals.collectAsState()

    val isLoading = categories.isEmpty() && areas.isEmpty()

    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Explore Recipes") })
//        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Text("Filter by Category", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                CategoryChips(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.onCategorySelected(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Filter by Area", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                AreaChips(
                    areas = areas,
                    selectedArea = selectedArea,
                    onAreaSelected = { viewModel.onAreaSelected(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                if (isLoading && filteredMeals.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else {
                    MealList(
                        meals = filteredMeals,
                        navController = navController
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(
    categories: List<com.example.recipefinderapp.domain.model.Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val allCategory = com.example.recipefinderapp.domain.model.Category("all", "All")
    val displayCategories = listOf(allCategory) + categories

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(displayCategories) { category ->
            FilterChip(
                selected = selectedCategory == category.name,
                onClick = { onCategorySelected(category.name) },
                label = { Text(category.name) },
                leadingIcon = if (selectedCategory == category.name) {
                    { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
                } else null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaChips(
    areas: List<String>,
    selectedArea: String,
    onAreaSelected: (String) -> Unit
) {
    val displayAreas = listOf("All") + areas

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(displayAreas) { area ->
            FilterChip(
                selected = selectedArea == area,
                onClick = { onAreaSelected(area) },
                label = { Text(area) },
                leadingIcon = if (selectedArea == area) {
                    { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
                } else null
            )
        }
    }
}

@Composable
fun MealList(
    meals: List<Meal>,
    navController: NavHostController
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(meals) { meal ->
            MealListItem(meal = meal, onClick = {
                navController.navigate(Screen.Detail.createRoute(meal.id))
            })
        }
    }
}

@Composable
fun MealListItem(
    meal: Meal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = meal.image,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}