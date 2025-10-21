@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipefinderapp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        content = { padding ->
            HomeContent(
                Modifier.padding(padding),
                navController = navController
            )
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { PopularMealsSection(navController) }
        item { RandomMealSection(navController) }
        item { CategorySection(navController) }
        item { AreaSection(navController) }
    }
}
@Composable
fun PopularMealsSection(
    navController: NavController
){
    val popularMeals = listOf(
        Meal("1", "Pasta", "Italian", "Europe", "Boil water...", "https://www.themealdb.com/images/media/meals/wvqpwt1468339226.jpg"),
        Meal("2", "Sushi", "Japanese", "Asia", "Roll rice...", "https://yujinizakaya.com.sg/wp-content/uploads/2025/06/japanese-nigiri-sushi-recipe-1749130962.jpg"),
        Meal("3", "Burger", "American", "USA", "Grill patty...", "https://www.themealdb.com/images/media/meals/k420tj1585565244.jpg")
    )

    Column {
        Text("Popular Meals", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(popularMeals){ meal->
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .clickable {
                            navController.navigate(Screen.Detail.createRoute(meal.id))
                        }
                ) {
                    Column {
                        AsyncImage(
                            model = meal.image,
                            contentDescription = meal.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(100.dp).fillMaxWidth()
                        )
                        Text(
                            text = meal.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RandomMealSection(navController: NavController){
    val randomMeal = Meal(
        id = "99",
        name = "Chef’s Surprise",
        category = "Mystery",
        area = "World",
        instructions = "Discover a surprise recipe!",
        image = "https://www.themealdb.com/images/media/meals/x0lk931587671540.jpg"
    )

    Column {
        Text("Suggestion", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.Detail.createRoute(randomMeal.id)) }
        ) {
            Column {
                AsyncImage(
                    model = randomMeal.image,
                    contentDescription = randomMeal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
                Text(
                    text = randomMeal.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun CategorySection(navController: NavController){
    val categories = listOf("Beef", "Chicken", "Dessert", "Seafood", "Vegetarian")
    Column {
        Text("Categories", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            items(categories){ category->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate("category/$category")
                        }

                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AreaSection(navController: NavController) {
    val areas = listOf("Italian", "Japanese", "American", "Indian", "Mexican")

    Column {
        Text("Area", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(areas) { area ->
                Card(
                    modifier = Modifier
                        .clickable { navController.navigate("area/$area") }
                ) {
                    Text(
                        text = area,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}