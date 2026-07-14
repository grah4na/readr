package com.readr.app.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.readr.app.data.model.ReadingEntry
import com.readr.app.ui.theme.DarkCharcoal
import com.readr.app.ui.theme.DarkGreen
import com.readr.app.ui.theme.MediumGrey
import com.readr.app.ui.theme.OffWhite
import com.readr.app.ui.theme.PrimaryYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookListItem(
    entry: ReadingEntry,
    onStartReading: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = OffWhite),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = if (entry.coverUrl.isNotBlank()) entry.coverUrl else null,
                contentDescription = entry.title,
                modifier = Modifier
                    .width(60.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkCharcoal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = entry.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MediumGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entry.dateFinished > 0) {
                    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    Text(
                        text = "Finished: ${dateFormat.format(Date(entry.dateFinished))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MediumGrey
                    )
                }
                if (entry.dateAdded > 0 && entry.dateFinished == 0L) {
                    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    Text(
                        text = "Added: ${dateFormat.format(Date(entry.dateAdded))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MediumGrey
                    )
                }
                if (entry.rating > 0 && entry.dateFinished > 0) {
                    Text(
                        text = "★".repeat(entry.rating) + "☆".repeat(5 - entry.rating),
                        fontSize = 12.sp,
                        color = PrimaryYellow
                    )
                }
            }
            if (onStartReading != null) {
                OutlinedButton(
                    onClick = onStartReading,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen),
                    modifier = Modifier.widthIn(min = 90.dp)
                ) {
                    Text("Start Reading", fontSize = 11.sp)
                }
            }
        }
    }
}
