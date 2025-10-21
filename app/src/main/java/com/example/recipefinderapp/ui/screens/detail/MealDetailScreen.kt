package com.example.recipefinderapp.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.example.recipefinderapp.domain.model.Meal

private val sampleMeals = listOf(
    Meal(
        id = "1",
        name = "Spaghetti Bolognese",
        category = "Pasta",
        image = "https://www.themealdb.com/images/media/meals/sutysw1468247559.jpg",
        instructions = "Cook pasta. Add sauce. Serve hot."
    ),
    Meal(
        id = "2",
        name = "Grilled Chicken",
        category = "Chicken",
        image = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
        instructions = "Grill the chicken until golden brown."
    )
)

fun getMealById(mealId: String?): Meal? {
    return sampleMeals.find { it.id == mealId }
}

@ExperimentalMaterial3Api
@Composable
fun MealDetailScreen(navController: NavController, mealId: String?) {
    val meal = getMealById(mealId)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(meal?.name ?: "Meal Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        meal?.let {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = rememberAsyncImagePainter(meal.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Category: ${meal.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                meal.instructions?.let { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Meal not found")
            }
        }
    }
}

