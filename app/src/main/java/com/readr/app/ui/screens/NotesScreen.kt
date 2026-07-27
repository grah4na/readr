package com.readr.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readr.app.data.local.entity.NoteEntity
import com.readr.app.data.model.ReadingEntry
import com.readr.app.ui.components.AddNoteSheet
import com.readr.app.ui.theme.VibrantBlue
import com.readr.app.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    viewModel: NotesViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("All Notes", "Highlights", "Favorites")

    val notes by viewModel.notes.collectAsState()
    val entries by viewModel.entries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showEntryPicker by remember { mutableStateOf(false) }
    var showAddNoteSheet by remember { mutableStateOf(false) }
    var noteTargetEntry by remember { mutableStateOf<ReadingEntry?>(null) }

    val filteredNotes = when (selectedTab) {
        1 -> notes.filter { it.type == "HIGHLIGHT" }
        2 -> notes.filter { it.tags?.contains("favorite", ignoreCase = true) == true }
        else -> notes
    }

    val entryMap = remember(entries) {
        entries.associateBy { it.id.toString() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 96.dp),
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
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {},
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = MaterialTheme.colorScheme.primary
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

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                    }
                }
            } else if (filteredNotes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No notes yet. Tap \"New Note\" to add one.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                items(filteredNotes, key = { it.id }) { note ->
                    val entry = entryMap[note.readingLogId]
                    NoteCardWithEntry(
                        note = note,
                        entry = entry,
                        onClick = {
                            entry?.let { onNavigateToDetail(it.id) }
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { showEntryPicker = true },
                modifier = Modifier
                    .widthIn(min = 200.dp)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Note", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEntryPicker) {
        AlertDialog(
            onDismissRequest = { showEntryPicker = false },
            title = { Text("Choose a book to add a note to") },
            text = {
                if (entries.isEmpty()) {
                    Text(
                        "No books found. Add a book first.",
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.Gray
                    )
                } else {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        entries.forEach { entry ->
                            TextButton(
                                onClick = {
                                    noteTargetEntry = entry
                                    showEntryPicker = false
                                    showAddNoteSheet = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = entry.title,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEntryPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAddNoteSheet && noteTargetEntry != null) {
        AddNoteSheet(
            onDismiss = {
                showAddNoteSheet = false
                noteTargetEntry = null
            },
            onSave = { text, pageNumber, tags, type ->
                noteTargetEntry?.let { entry ->
                    viewModel.addNote(entry.id.toString(), text, pageNumber, tags, type)
                }
                showAddNoteSheet = false
                noteTargetEntry = null
            }
        )
    }
}

@Composable
private fun NoteCardWithEntry(
    note: NoteEntity,
    entry: ReadingEntry?,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (entry != null) {
                Text(
                    text = "Book: ${entry.title}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateFormat.format(Date(note.createdAt)),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )
        }
    }
}
