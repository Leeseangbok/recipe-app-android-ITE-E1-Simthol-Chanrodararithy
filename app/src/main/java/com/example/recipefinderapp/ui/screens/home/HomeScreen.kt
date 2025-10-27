@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipefinderapp.ui.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipefinderapp.R
import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val popularMeals by viewModel.meals.collectAsState()
    val randomMeal by viewModel.randomMeal.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val areas by viewModel.areas.collectAsState()

    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Recipe Finder",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
            Divider(color = Color.LightGray, thickness = 1.dp)

            HomeContent(
                modifier = Modifier.padding(),
                navController = navController,
                popularMeals = popularMeals,
                randomMeal = randomMeal,
                categories = categories,
                areas = areas
            )
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    modifier: Modifier,
    popularMeals: List<Meal>,
    randomMeal: Meal?,
    categories: List<Category>,
    areas: List<String>
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        item { RandomMealSection(navController, randomMeal) }
        item { PopularMealsSection(navController, popularMeals) }
        item { CategorySection(navController, categories) }
        item { AreaSection(navController, areas) }
    }
}

@Composable
fun RandomMealSection(navController: NavController, randomMeal: Meal?) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionTitle("Today's Suggestion")
        Spacer(Modifier.height(8.dp))

        if (randomMeal == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.Detail.createRoute(randomMeal.id))
                    },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(randomMeal.image)
                            .crossfade(true)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .build(),
                        contentDescription = randomMeal.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Text(
                        text = randomMeal.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PopularMealsSection(navController: NavController, popularMeals: List<Meal>) {
    val mealsToShow = popularMeals.take(6)
    Column {
        SectionTitle("Popular Meals")
        Spacer(Modifier.height(8.dp))

        if (popularMeals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mealsToShow) { meal ->
                    Log.d("HomeScreen", "Meal Image URL: ${meal.image}")
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .clickable {
                                navController.navigate(Screen.Detail.createRoute(meal.id))
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(meal.image)
                                    .crossfade(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .build(),
                                contentDescription = meal.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            )
                            Text(
                                text = meal.name ?: "",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySection(navController: NavController, categories: List<Category>) {
    Column {
        SectionTitle("Categories")
        Spacer(Modifier.height(8.dp))

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .clickable {
                                navController.navigate(Screen.Explore.createRoute(category = category.name))
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(category.image)
                                    .crossfade(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .build(),
                                contentDescription = category.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    category.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Filter ${category.name}",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaSection(navController: NavController, areas: List<String>) {
    Column {
        SectionTitle("Cuisines (Area)")
        Spacer(Modifier.height(8.dp))

        if (areas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(areas) { area ->
                    AssistChip(
                        onClick = {
                            navController.navigate(Screen.Explore.createRoute(area = area))
                        },
                        label = { Text(area) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Filter $area"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
