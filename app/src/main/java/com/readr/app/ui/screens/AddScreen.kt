package com.readr.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.ui.theme.DarkGreen
import com.readr.app.ui.theme.LightSage
import com.readr.app.ui.theme.PrimaryYellow
import com.readr.app.ui.theme.SoftBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    onNavigateToManualEntry: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("Fiction") }
    var format by remember { mutableStateOf("Paperback") }
    var rating by remember { mutableIntStateOf(0) }
    var thoughts by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 100.dp, top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Add a new \nbook to your shelf",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Book Details",
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cover Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp, 150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightSage.copy(alpha = 0.3f))
                        .border(1.dp, LightSage, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = DarkGreen)
                        Text("Add Cover", style = MaterialTheme.typography.labelSmall, color = DarkGreen)
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SimpleTextField(value = title, onValueChange = { title = it }, label = "Title")
                    SimpleTextField(value = author, onValueChange = { author = it }, label = "Author")
                    DropdownField(value = genre, label = "Genre")
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DateField(label = "Start Date", modifier = Modifier.weight(1f))
                DateField(label = "End Date", modifier = Modifier.weight(1f))
            }
        }

        item {
            DropdownField(value = format, label = "Format")
        }

        item {
            Column {
                Text("Your Rating", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    repeat(5) { index ->
                        IconButton(onClick = { rating = index + 1 }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = if (index < rating) PrimaryYellow else Color.LightGray
                            )
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("My Thoughts (optional)", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = thoughts,
                    onValueChange = { thoughts = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Share your thoughts...", fontSize = 14.sp) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = SoftBeige,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = DarkGreen
                    )
                )
            }
        }

        item {
            Button(
                onClick = { /* Save */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Add to Library", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f),
                focusedIndicatorColor = DarkGreen
            ),
            singleLine = true
        )
    }
}

@Composable
fun DropdownField(value: String, label: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(value, style = MaterialTheme.typography.bodyMedium)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun DateField(label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Select Date", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        }
    }
}
