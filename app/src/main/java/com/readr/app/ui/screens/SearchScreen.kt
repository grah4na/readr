package com.readr.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.readr.app.data.model.SearchResult
import com.readr.app.ui.theme.VibrantBlue
import com.readr.app.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateToManualEntry: () -> Unit = {},
    viewModel: SearchViewModel = viewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val scope = rememberCoroutineScope()

    val popularSearches = listOf(
        "Fantasy", "Romance", "Science Fiction",
        "Self Help", "Mystery", "Thriller",
        "Classics", "Poetry"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 100.dp, top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Find your \nnext favorite book",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by title, author or genre", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )
        }

        if (query.isEmpty()) {
            item {
                Text(
                    text = "Popular Searches",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp
                ) {
                    popularSearches.forEach { genre ->
                        SuggestionChip(
                            onClick = { viewModel.updateQuery(genre) },
                            label = { Text(genre, fontSize = 12.sp) },
                            shape = RoundedCornerShape(8.dp),
                            border = SuggestionChipDefaults.suggestionChipBorder(borderColor = Color.LightGray.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Recommended for you",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Dummy recommendation if no search
            items(3) { index ->
                RecommendedBookItemPlaceholder(index)
            }
        } else {
            if (isSearching) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(results) { result ->
                    SearchResultItem(
                        result = result,
                        onWantToRead = {
                            scope.launch {
                                viewModel.addToWantToRead(result)
                            }
                        },
                        onStartReading = {
                            scope.launch {
                                viewModel.addToCurrentlyReading(result)
                            }
                        },
                        onMarkFinished = {
                            scope.launch {
                                viewModel.addToFinished(result)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    crossAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        var y = 0
        var x = 0
        var maxY = 0
        var totalHeight = 0
        for (placeable in placeables) {
            if (x + placeable.width > constraints.maxWidth) {
                x = 0
                totalHeight += maxY + crossAxisSpacing.roundToPx()
                y = totalHeight
                maxY = 0
            }
            x += placeable.width + mainAxisSpacing.roundToPx()
            maxY = maxOf(maxY, placeable.height)
        }
        totalHeight += maxY
        layout(constraints.maxWidth, totalHeight) {
            y = 0
            x = 0
            maxY = 0
            placeables.forEach { placeable ->
                if (x + placeable.width > constraints.maxWidth) {
                    x = 0
                    y += maxY + crossAxisSpacing.roundToPx()
                    maxY = 0
                }
                placeable.placeRelative(x, y)
                x += placeable.width + mainAxisSpacing.roundToPx()
                maxY = maxOf(maxY, placeable.height)
            }
        }
    }
}

@Composable
fun RecommendedBookItemPlaceholder(index: Int) {
    val titles = listOf("The Seven Husbands of Evelyn Hugo", "It Ends With Us", "Verity")
    val authors = listOf("Taylor Jenkins Reid", "Colleen Hoover", "Colleen Hoover")
    val ratings = listOf(4.6, 4.5, 4.3)
    val covers = listOf(
        "https://covers.openlibrary.org/b/isbn/9781501161933-L.jpg",
        "https://covers.openlibrary.org/b/isbn/9781501110368-L.jpg",
        "https://covers.openlibrary.org/b/isbn/9781538724736-L.jpg"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = covers[index % covers.size],
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titles[index % titles.size],
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = authors[index % authors.size],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                    Text(
                        text = " ${ratings[index % ratings.size]}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun SearchResultItem(
    result: SearchResult,
    onWantToRead: () -> Unit,
    onStartReading: () -> Unit,
    onMarkFinished: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = result.coverUrl.ifBlank { null },
                contentDescription = null,
                modifier = Modifier
                    .width(54.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = result.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${result.pages} pages",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilledTonalButton(
                        onClick = onWantToRead,
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Want to Read", fontSize = 9.sp, maxLines = 1)
                    }
                    FilledTonalButton(
                        onClick = onStartReading,
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Reading", fontSize = 9.sp, maxLines = 1)
                    }
                    FilledTonalButton(
                        onClick = onMarkFinished,
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Finished", fontSize = 9.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}
