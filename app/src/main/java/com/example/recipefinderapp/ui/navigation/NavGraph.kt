package com.example.recipefinderapp.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipefinderapp.ui.screens.detail.MealDetailScreen
import com.example.recipefinderapp.ui.screens.explore.ExploreScreen
import com.example.recipefinderapp.ui.screens.favorite.FavoriteScreen
import com.example.recipefinderapp.ui.screens.home.HomeScreen
import com.example.recipefinderapp.ui.screens.onboarding.OnboardingScreen

@ExperimentalMaterial3Api
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Explore.route) {
            ExploreScreen(navController)
        }
        composable(Screen.Favorites.route) {
            FavoriteScreen(navController)
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ){ backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            MealDetailScreen(navController = navController, mealId = mealId)
        }
    }
}