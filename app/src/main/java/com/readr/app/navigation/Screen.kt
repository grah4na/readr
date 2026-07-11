package com.readr.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Add : Screen("add", "Create", Icons.Default.AddCircle)
    object Notes : Screen("notes", "Notes", Icons.Default.Description)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object ManualEntry : Screen("manual_entry", "", Icons.Default.AddCircle)
    object EntryDetail : Screen("entry_detail/{entryId}", "", Icons.Default.Home) {
        fun createRoute(entryId: Long) = "entry_detail/$entryId"
    }

    companion object {
        val items = listOf(Home, Search, Add, Notes, Profile)
    }
}
