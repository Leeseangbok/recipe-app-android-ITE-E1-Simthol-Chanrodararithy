@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipefinderapp.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipefinderapp.R
import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.ui.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val popularMeals by viewModel.meals.collectAsState()
    val randomMeal by viewModel.randomMeal.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val areas by viewModel.areas.collectAsState()

    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Recipe Finder") })
//        },
        content = { padding ->
            HomeContent(
                modifier = Modifier.padding(padding),
                navController = navController,
                popularMeals = popularMeals,
                randomMeal = randomMeal,
                categories = categories,
                areas = areas // <-- Pass areas
            )
        }
    )
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
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { RandomMealSection(navController, randomMeal) }
        item { PopularMealsSection(navController, popularMeals) }
        item { CategorySection(navController, categories) }
        item { AreaSection(navController, areas) }
    }
}
@Composable
fun PopularMealsSection(
    navController: NavController,
    popularMeals: List<Meal>
){
    val mealsToShow = popularMeals.take(5)
    Column {
        Text(
            text = "Popular Meals",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        if(popularMeals.isEmpty()){
            Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }else{
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mealsToShow){ meal->
                    Log.d("HomeScreen", "Meal Image URL: ${meal.image}")
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .clickable {
                                navController.navigate(Screen.Detail.createRoute(meal.id))
                            }
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
                                    .height(150.dp)
                            )
                            Text(
                                text = meal.name ?: "",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RandomMealSection(navController: NavController, randomMeal: Meal?){
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Suggestion",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(8.dp))

        if(randomMeal!=null){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Detail.createRoute(randomMeal.id)) }
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
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                    )
                    Text(
                        text = randomMeal.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        else{
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySection(
    navController: NavController,
    categories: List<Category>
){
    Column {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        if(categories.isEmpty()){
            CircularProgressIndicator(modifier = Modifier.padding(start = 16.dp))
        }else{
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(categories){ category->
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .clickable {
                                navController.navigate(Screen.Explore.createRoute(category = category.name))
                            }
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
                                    .height(150.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(category.name)
                                Icon(Icons.Default.ChevronRight, contentDescription = "Filter ${category.name}")
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
fun AreaSection(
    navController: NavController,
    areas: List<String>
) {
    Column {
        Text(
            text = "Cuisines (Area)",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        if(areas.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(start = 16.dp).size(24.dp))
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
                            Icon(Icons.Default.ChevronRight, contentDescription = "Filter $area")
                        }
                    )
                }
            }
        }
    }
}