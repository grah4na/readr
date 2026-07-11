package com.readr.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.readr.app.data.local.entity.NoteEntity
import com.readr.app.data.local.entity.QuoteEntity
import com.readr.app.data.local.entity.ReviewEntity
import com.readr.app.data.model.ReadingSession
import com.readr.app.ui.components.AddNoteSheet
import com.readr.app.ui.components.AddQuoteBottomSheet
import com.readr.app.ui.components.AddReviewSheet
import com.readr.app.ui.components.BookPreview
import com.readr.app.ui.components.CommunityNotesBanner
import com.readr.app.ui.components.NoteCard
import com.readr.app.ui.components.QuoteCard
import com.readr.app.ui.components.ReviewCard
import com.readr.app.viewmodel.EntryDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDetailScreen(
    entryId: Long,
    onNavigateBack: () -> Unit = {},
    viewModel: EntryDetailViewModel = viewModel()
) {
    LaunchedEffect(entryId) {
        viewModel.loadEntry(entryId)
    }

    val entry by viewModel.entry.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val quotes by viewModel.quotes.collectAsState()
    val review by viewModel.review.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val communityNotes by viewModel.communityNotes.collectAsState()

    var showAddSessionDialog by remember { mutableStateOf(false) }
    var showAddQuoteSheet by remember { mutableStateOf(false) }
    var showAddReviewSheet by remember { mutableStateOf(false) }
    var showAddNoteSheet by remember { mutableStateOf(false) }

    val tabs = listOf("Quotes", "Reviews", "Notes", "Preview")
    var selectedTab by remember { mutableIntStateOf(0) }

    if (showAddSessionDialog) {
        AddSessionDialog(
            onDismiss = { showAddSessionDialog = false },
            onConfirm = { pages, duration, notes ->
                viewModel.addSession(pages, duration, notes)
                showAddSessionDialog = false
            }
        )
    }
    if (showAddQuoteSheet) {
        AddQuoteBottomSheet(
            onDismiss = { showAddQuoteSheet = false },
            onSave = { text, pageNumber ->
                viewModel.addQuote(text, pageNumber)
                showAddQuoteSheet = false
            }
        )
    }
    if (showAddReviewSheet) {
        AddReviewSheet(
            onDismiss = { showAddReviewSheet = false },
            onSave = { rating, reviewText, spoilerPercent, whatILearned ->
                viewModel.addReview(rating, reviewText, spoilerPercent, whatILearned)
                showAddReviewSheet = false
            }
        )
    }
    if (showAddNoteSheet) {
        AddNoteSheet(
            onDismiss = { showAddNoteSheet = false },
            onSave = { text, pageNumber, tags, type ->
                viewModel.addNote(text, pageNumber, tags, type)
                showAddNoteSheet = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(entry?.title ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (entry == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Entry not found")
            }
        } else {
            val e = entry!!
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = e.coverUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp, 120.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = e.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = e.author,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (e.isbn.isNotBlank()) {
                                    Text(
                                        text = "ISBN: ${e.isbn}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (e.pages > 0) {
                                    Text(
                                        text = "${e.pages} pages",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        "About this book",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (e.description.isNotBlank()) {
                        Text(
                            text = e.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            "No description available",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                item {
                    Text(
                        "Progress",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = e.progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${(e.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.updateProgress((e.progress + 0.1f).coerceAtMost(1f))
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("+10%", style = MaterialTheme.typography.labelSmall)
                            }
                            if (e.progress < 1f) {
                                Button(
                                    onClick = { viewModel.markAsFinished() },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Finish", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (i in 1..5) {
                            IconButton(onClick = { viewModel.updateRating(i) }) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Star $i",
                                    tint = if (i <= e.rating)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Reading Sessions",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        FilledTonalButton(
                            onClick = { showAddSessionDialog = true },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Log Session", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                if (sessions.isEmpty()) {
                    item {
                        Text(
                            "No sessions logged yet",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(sessions) { session ->
                        SessionCard(
                            session = session,
                            onDelete = { viewModel.deleteSession(session) }
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CommunityNotesBanner(communityNotes = communityNotes)

                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = Color(0xFFFCC024)
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = {
                                        Text(
                                            text = title,
                                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedTab == index) Color(0xFFFCC024) else Color.Gray
                                        )
                                    }
                                )
                            }
                        }

                        when (selectedTab) {
                            0 -> QuotesTab(
                                quotes = quotes,
                                onAddClick = { showAddQuoteSheet = true }
                            )
                            1 -> ReviewsTab(
                                review = review,
                                currentProgress = e.progress,
                                onAddClick = { showAddReviewSheet = true }
                            )
                            2 -> NotesTab(
                                notes = notes,
                                onAddClick = { showAddNoteSheet = true },
                                onSearch = { viewModel.searchNotes(it) }
                            )
                            3 -> PreviewTab(
                                previewUrl = e.previewUrl,
                                title = e.title
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuotesTab(
    quotes: List<QuoteEntity>,
    onAddClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCC024)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Quote", color = Color.Black, style = MaterialTheme.typography.labelSmall)
            }
        }
        if (quotes.isEmpty()) {
            Text(
                "No quotes saved yet",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            quotes.forEach { quote ->
                QuoteCard(quote = quote)
            }
        }
    }
}

@Composable
private fun ReviewsTab(
    review: ReviewEntity?,
    currentProgress: Float,
    onAddClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCC024)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Review", color = Color.Black, style = MaterialTheme.typography.labelSmall)
            }
        }
        if (review == null) {
            Text(
                "No review written yet",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            ReviewCard(
                review = review,
                currentProgress = currentProgress,
                onRevealSpoiler = {}
            )
        }
    }
}

@Composable
private fun NotesTab(
    notes: List<NoteEntity>,
    onAddClick: () -> Unit,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCC024)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Note", color = Color.Black, style = MaterialTheme.typography.labelSmall)
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            label = { Text("Search notes...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (notes.isEmpty()) {
            Text(
                "No notes yet",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            notes.forEach { note ->
                NoteCard(note = note)
            }
        }
    }
}

@Composable
private fun PreviewTab(
    previewUrl: String?,
    title: String
) {
    BookPreview(
        previewUrl = previewUrl,
        title = title
    )
}

@Composable
fun SessionCard(
    session: ReadingSession,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateFormat.format(Date(session.date)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (session.pagesRead > 0) {
                        Text(
                            "${session.pagesRead} pages",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (session.durationMinutes > 0) {
                        Text(
                            "${session.durationMinutes} min",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (session.notes.isNotBlank()) {
                    Text(
                        text = session.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete session",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddSessionDialog(
    onDismiss: () -> Unit,
    onConfirm: (pages: Int, duration: Int, notes: String) -> Unit
) {
    var pages by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Reading Session") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = pages,
                    onValueChange = { pages = it.filter { c -> c.isDigit() } },
                    label = { Text("Pages Read") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { c -> c.isDigit() } },
                    label = { Text("Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        pages.toIntOrNull() ?: 0,
                        duration.toIntOrNull() ?: 0,
                        notesText.trim()
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
