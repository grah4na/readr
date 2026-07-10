package com.readr.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.readr.app.data.model.EntryType
import com.readr.app.data.model.ReadingEntry
import com.readr.app.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val currentlyReading by viewModel.currentlyReading.collectAsState()
    val wantToRead by viewModel.wantToRead.collectAsState()
    val finished by viewModel.finished.collectAsState()
    val allEntries by viewModel.allEntries.collectAsState()

    if (allEntries.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Welcome to Readr",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Search for books or add manually to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (currentlyReading.isNotEmpty()) {
            item {
                Text(
                    text = "Currently Reading",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(currentlyReading) { entry ->
                CurrentlyReadingCard(
                    entry = entry,
                    onClick = { onNavigateToDetail(entry.id) }
                )
            }
        }

        if (wantToRead.isNotEmpty()) {
            item {
                SectionHeader("Want to Read")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(wantToRead) { entry ->
                        BookItem(
                            entry = entry,
                            onClick = { onNavigateToDetail(entry.id) }
                        )
                    }
                }
            }
        }

        if (finished.isNotEmpty()) {
            item {
                SectionHeader("Finished")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(finished) { entry ->
                        BookItem(
                            entry = entry,
                            onClick = { onNavigateToDetail(entry.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentlyReadingCard(
    entry: ReadingEntry,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = entry.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = entry.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = entry.progress,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${(entry.progress * 100).toInt()}% finished",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    entry: ReadingEntry,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.width(120.dp)) {
        Card(
            onClick = onClick,
            modifier = Modifier.size(120.dp, 180.dp)
        ) {
            AsyncImage(
                model = entry.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = entry.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = entry.author,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
