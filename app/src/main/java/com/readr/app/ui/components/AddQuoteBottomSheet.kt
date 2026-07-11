package com.readr.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteBottomSheet(
    onDismiss: () -> Unit,
    onSave: (text: String, pageNumber: Int?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var text by remember { mutableStateOf("") }
    var pageNumber by remember { mutableStateOf("") }

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
                text = "Add Quote",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.take(420) },
                label = { Text("Quote text") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                supportingText = {
                    Text("${text.length}/420")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pageNumber,
                onValueChange = { pageNumber = it.filter { c -> c.isDigit() } },
                label = { Text("Page number (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSave(text, pageNumber.toIntOrNull())
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFCC024)
                )
            ) {
                Text("Save Quote", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
