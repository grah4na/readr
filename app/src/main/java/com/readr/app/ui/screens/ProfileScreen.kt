package com.readr.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.ui.theme.DarkGreen
import com.readr.app.ui.theme.OffWhite
import com.readr.app.ui.theme.PrimaryYellow

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item { ProfileHeader() }

        item {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                ReadingGoalsSection()
                ReadingStatsSection()
                AchievementsSection()
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(DarkGreen)
            .padding(top = 48.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, PrimaryYellow, CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder image
                Text("B", color = Color.White, fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Booklover 🖋️",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = "Always traveling through stories.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("38", "Books Read")
                StatItem("12", "Currently Reading")
                StatItem("56", "Want to Read")
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
    }
}

@Composable
fun ReadingGoalsSection() {
    Column {
        Text("Reading Goals", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = OffWhite),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("2025 Goal", style = MaterialTheme.typography.labelMedium)
                    Text("35/50 books", style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 0.7f,
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryYellow,
                    trackColor = PrimaryYellow.copy(alpha = 0.2f)
                )
                Text(
                    text = "70%",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ReadingStatsSection() {
    Column {
        Text("Reading Stats", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SmallStatCard("Pages Read", "12,540", Modifier.weight(1f))
            SmallStatCard("Hours Spent", "320", Modifier.weight(1f))
            SmallStatCard("Avg. Rating", "4.3 ★", Modifier.weight(1f))
        }
    }
}

@Composable
fun SmallStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = OffWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 10.sp, color = Color.Gray)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AchievementsSection() {
    Column {
        Text("Achievements", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryYellow.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = PrimaryYellow, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
