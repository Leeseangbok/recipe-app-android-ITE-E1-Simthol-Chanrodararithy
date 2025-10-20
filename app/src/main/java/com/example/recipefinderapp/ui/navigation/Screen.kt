package com.example.recipefinderapp.ui.navigation

sealed class Screen(val route: String, val title: String, val icon: String) {
    data object Home : Screen("home", "Home", "ic_home")
    data object Explore : Screen("explore", "Explore", "ic_explore")
    data object Favorites : Screen("favorites", "Favorites", "ic_favorites")
    data object Detail : Screen("detail", "Detail", "ic_detail")
}