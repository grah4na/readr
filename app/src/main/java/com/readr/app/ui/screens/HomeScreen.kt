package com.readr.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.readr.app.data.model.ReadingEntry
import com.readr.app.ui.theme.PrimaryYellow
import com.readr.app.ui.theme.SoftBeige
import com.readr.app.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val wantToRead by viewModel.wantToRead.collectAsState()
    val finished by viewModel.finished.collectAsState()
    val allEntries by viewModel.allEntries.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp, top = 24.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { HomeHeader() }

        if (wantToRead.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Want to Read",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(wantToRead) { entry ->
                        WantToReadItem(
                            entry = entry,
                            onClick = { onNavigateToDetail(entry.id) }
                        )
                    }
                }
            }
        }

        if (finished.isNotEmpty()) {
            item {
                Text(
                    text = "Finished",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(finished) { entry ->
                FinishedBookCard(
                    entry = entry,
                    onClick = { onNavigateToDetail(entry.id) }
                )
            }
        }

        item {
            ReadingStreakCard()
        }
    }
}

@Composable
fun HomeHeader() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good morning,",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Reader ☀️",
                    style = MaterialTheme.typography.displayLarge
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("JD")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ready for a new adventure?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WantToReadItem(
    entry: ReadingEntry,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.size(100.dp, 150.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            AsyncImage(
                model = entry.coverUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = entry.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = entry.author,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedBookCard(
    entry: ReadingEntry,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            onClick = onClick,
            modifier = Modifier.size(100.dp, 150.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            AsyncImage(
                model = entry.coverUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = entry.title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = entry.author,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ReadingStreakCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SoftBeige),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryYellow.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = PrimaryYellow,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Reading Streak",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "7 days in a row",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
