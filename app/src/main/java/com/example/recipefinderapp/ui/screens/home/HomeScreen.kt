@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipefinderapp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipefinderapp.domain.model.Meal

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Recipe Finder") }) },
        content = { padding ->
            HomeContent(Modifier.padding(padding))
        }
    )
}

@Composable
fun HomeContent(modifier: Modifier = Modifier){
    val dummyMeals = listOf(
        Meal("1", "Pasta", "Italian", "Europe", "Boil water...", "https://via.placeholder.com/150"),
        Meal("2", "Sushi", "Japanese", "Asia", "Roll rice...", "https://via.placeholder.com/150")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(dummyMeals.size) { index ->
            val meal = dummyMeals[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                onClick = {}
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AsyncImage(
                        model = meal.image,
                        contentDescription = meal.name,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(meal.name, style = MaterialTheme.typography.titleMedium)
                        Text(meal.category ?: "")
                    }
                }
            }
        }
    }

}