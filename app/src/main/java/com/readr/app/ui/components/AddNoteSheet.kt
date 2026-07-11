package com.readr.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteSheet(
    onDismiss: () -> Unit,
    onSave: (text: String, pageNumber: Int?, tags: String, type: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var text by remember { mutableStateOf("") }
    var pageNumber by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("WHAT_I_LEARNED") }

    val noteTypes = listOf(
        "WHAT_I_LEARNED" to "What I Learned",
        "PACING" to "Pacing Note",
        "TRIGGER_WARNING" to "Trigger Warning"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Add Note",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Note text") },
                placeholder = { Text("Markdown supported") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = pageNumber,
                onValueChange = { pageNumber = it.filter { c -> c.isDigit() } },
                label = { Text("Page number (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags (comma-separated, e.g. theme, character, plot)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Note type",
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium
            )
            Column(modifier = Modifier.selectableGroup()) {
                noteTypes.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedType == value,
                                onClick = { selectedType = value },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == value,
                            onClick = null
                        )
                        Text(label)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSave(
                            text.trim(),
                            pageNumber.toIntOrNull(),
                            tags.trim(),
                            selectedType
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFCC024)
                )
            ) {
                Text("Save Note", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
