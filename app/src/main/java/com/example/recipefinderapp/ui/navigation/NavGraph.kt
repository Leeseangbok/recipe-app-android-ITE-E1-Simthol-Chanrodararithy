package com.example.recipefinderapp.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.recipefinderapp.ui.screens.detail.MealDetailScreen
import com.example.recipefinderapp.ui.screens.explore.ExploreScreen
import com.example.recipefinderapp.ui.screens.favorite.FavoriteScreen
import com.example.recipefinderapp.ui.screens.home.HomeScreen

@ExperimentalMaterial3Api
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route){
            HomeScreen(navController)
        }
        composable(Screen.Explore.route) {
            ExploreScreen(navController)
        }
        composable(Screen.Favorites.route) {
            FavoriteScreen(navController)
        }
        composable(
            route = Screen.Detail.route + "/{mealId}"
        ){ backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            MealDetailScreen(navController, mealId)
        }
    }
}