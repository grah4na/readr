package com.readr.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.ui.theme.DarkGreen
import com.readr.app.ui.theme.PrimaryYellow
import com.readr.app.ui.theme.SoftBeige

@Composable
fun NotesScreen(
    onNavigateToDetail: (Long) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("All Notes", "Highlights", "Favorites")

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 100.dp, top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Your notes, \nyour reflections",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = DarkGreen,
                    divider = {},
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = DarkGreen
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }

            items(3) { index ->
                NoteCardPlaceholder(index)
            }
        }

        // Custom FAB
        Button(
            onClick = { /* New Note */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("New Note", fontSize = 14.sp)
        }
    }
}

@Composable
fun NoteCardPlaceholder(index: Int) {
    val quotes = listOf(
        "Books are a uniquely portable magic.",
        "We accept the love we think we deserve.",
        "It does not do to dwell on dreams and forget to live."
    )
    val authors = listOf("Stephen King", "Stephen Chbosky", "J.K. Rowling")
    val books = listOf("The Fault in Our Stars", "The Perks of Being a Wallflower", "Harry Potter and the Sorcerer's Stone")
    val dates = listOf("May 12, 2023", "May 10, 2023", "May 8, 2023")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SoftBeige),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("“", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                Icon(Icons.Default.Star, contentDescription = null, tint = PrimaryYellow, modifier = Modifier.size(18.dp))
            }
            Text(
                text = quotes[index % quotes.size],
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "— ${authors[index % authors.size]}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "On: ${books[index % books.size]}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = dates[index % dates.size],
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )
        }
    }
}
