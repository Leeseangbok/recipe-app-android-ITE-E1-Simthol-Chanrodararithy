@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipefinderapp.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.ui.navigation.Screen
import kotlinx.coroutines.launch

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
    val searchQuery by viewModel.searchQuery.collectAsState()

    val isLoading = categories.isEmpty() && areas.isEmpty()
    val lazyListState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        val category = navBackStackEntry?.arguments?.getString("category") ?: "All"
        val area = navBackStackEntry?.arguments?.getString("area") ?: "All"
        if (category != "All" || area != "All") {
            coroutineScope.launch { lazyListState.animateScrollToItem(0) }
        }
        viewModel.onCategorySelected(category)
        viewModel.onAreaSelected(area)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Explore",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp),
            textAlign = TextAlign.Center
        )
        Divider(color = Color.LightGray, thickness = 1.dp)

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Search recipes...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        viewModel.onSearchQueryChanged("")
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(28.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            singleLine = true
        )

        SectionTitle("Category")
        CategoryChips(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = {
                viewModel.onCategorySelected(it)
                coroutineScope.launch { lazyListState.animateScrollToItem(0) }
            }
        )

        SectionTitle("Area")
        AreaChips(
            areas = areas,
            selectedArea = selectedArea,
            onAreaSelected = {
                viewModel.onAreaSelected(it)
                coroutineScope.launch { lazyListState.animateScrollToItem(0) }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier = Modifier.padding(horizontal = 16.dp))

        when {
            isLoading && filteredMeals.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            filteredMeals.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No recipes found.", style = MaterialTheme.typography.bodyMedium)
            }

            else -> MealList(
                meals = filteredMeals,
                navController = navController,
                listState = lazyListState
            )
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
    )
}

@Composable
fun CategoryChips(
    categories: List<com.example.recipefinderapp.domain.model.Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val allCategory = com.example.recipefinderapp.domain.model.Category("all", "All")
    val displayCategories = listOf(allCategory) + categories

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
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

@Composable
fun AreaChips(
    areas: List<String>,
    selectedArea: String,
    onAreaSelected: (String) -> Unit
) {
    val displayAreas = listOf("All") + areas
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
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
    navController: NavHostController,
    listState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(meals) { meal ->
            MealListItem(meal = meal, onClick = {
                navController.navigate(Screen.Detail.createRoute(meal.id))
            })
        }
    }
}

@Composable
fun MealListItem(meal: Meal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            AsyncImage(
                model = meal.image,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(12.dp),
                maxLines = 1
            )
        }
    }
}
