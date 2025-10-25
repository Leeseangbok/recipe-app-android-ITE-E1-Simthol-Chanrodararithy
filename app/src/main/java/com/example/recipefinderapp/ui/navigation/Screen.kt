package com.example.recipefinderapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    data object Onboarding : Screen("onboarding", "Onboarding", null)
    data object Home : Screen("home", "Home", Icons.Filled.Home)

    data object Explore : Screen(
        route = "explore?category={category}&area={area}",
        title = "Explore",
        icon = Icons.Filled.Explore
    ) {
        fun createRoute(category: String? = null, area: String? = null): String {
            var route = "explore"
            val args = mutableListOf<String>()
            category?.let { args.add("category=$it") }
            area?.let { args.add("area=$it") }
            if (args.isNotEmpty()) {
                route += "?" + args.joinToString("&")
            }
            return route
        }
    }

    data object Favorites : Screen("favorites", "Favorites", Icons.Filled.Favorite)
    data object Detail : Screen("detail/{mealId}", "Detail", null){
        fun createRoute(mealId: String) = "detail/$mealId"
    }
}