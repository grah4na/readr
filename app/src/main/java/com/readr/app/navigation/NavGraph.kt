package com.readr.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.readr.app.ui.screens.AddScreen
import com.readr.app.ui.screens.EntryDetailScreen
import com.readr.app.ui.screens.HomeScreen
import com.readr.app.ui.screens.ManualEntryScreen
import com.readr.app.ui.screens.NotesScreen
import com.readr.app.ui.screens.SearchScreen
import com.readr.app.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateToManualEntry = {
                    navController.navigate(Screen.ManualEntry.route)
                }
            )
        }
        composable(Screen.Add.route) {
            AddScreen(
                onNavigateToManualEntry = {
                    navController.navigate(Screen.ManualEntry.route)
                }
            )
        }
        composable(Screen.Notes.route) {
            NotesScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.ManualEntry.route) {
            ManualEntryScreen(
                onNavigateBack = { navController.popBackStack() },
                onEntrySaved = { entryId ->
                    navController.popBackStack()
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                }
            )
        }
        composable(
            route = Screen.EntryDetail.route,
            arguments = listOf(navArgument("entryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getLong("entryId") ?: return@composable
            EntryDetailScreen(
                entryId = entryId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
