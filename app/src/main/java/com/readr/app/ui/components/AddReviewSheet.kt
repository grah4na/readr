package com.readr.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewSheet(
    onDismiss: () -> Unit,
    onSave: (rating: Int, reviewText: String, spoilerPercent: Float?, whatILearned: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var spoilerPercent by remember { mutableFloatStateOf(0f) }
    var whatILearned by remember { mutableStateOf("") }

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
                text = "Add Review",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Star $i",
                        tint = Color(0xFFFCC024),
                        modifier = Modifier
                            .width(36.dp)
                            .clickable { rating = i }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Review") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Spoiler control",
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Hide until reader reaches", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${(spoilerPercent * 100).toInt()}%",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
            Slider(
                value = spoilerPercent,
                onValueChange = { spoilerPercent = it },
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = whatILearned,
                onValueChange = { whatILearned = it },
                label = { Text("What I Learned (Markdown)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (rating > 0) {
                        onSave(
                            rating,
                            reviewText.trim(),
                            if (spoilerPercent > 0f) spoilerPercent else null,
                            whatILearned.trim()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = rating > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFCC024)
                )
            ) {
                Text("Save Review", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
