package com.readr.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readr.app.viewmodel.ManualEntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualEntryScreen(
    onNavigateBack: () -> Unit = {},
    onEntrySaved: (Long) -> Unit = {},
    viewModel: ManualEntryViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.savedEntryId) {
        if (state.savedEntryId != null && state.savedEntryId != 0L) {
            onEntrySaved(state.savedEntryId!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Manually") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.author,
                onValueChange = { viewModel.updateAuthor(it) },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.isbn,
                onValueChange = { viewModel.updateIsbn(it) },
                label = { Text("ISBN (auto-fetches on entry)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = if (state.isAutoFetching) {
                    { Text("Auto-fetching metadata...", color = MaterialTheme.colorScheme.primary) }
                } else null
            )

            OutlinedTextField(
                value = state.pages,
                onValueChange = { viewModel.updatePages(it) },
                label = { Text("Pages") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            state.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.save() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving && state.title.isNotBlank()
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Save to Library")
            }
        }
    }
}
