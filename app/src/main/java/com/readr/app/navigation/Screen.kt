package com.readr.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Add : Screen("add", "Add", Icons.Default.Add)
    object Notes : Screen("notes", "Notes", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)

    companion object {
        val items = listOf(Home, Search, Add, Notes, Profile)
    }
}
