package com.readr.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.readr.app.data.local.entity.ReviewEntity

@Composable
fun ReviewCard(
    review: ReviewEntity,
    currentProgress: Float,
    onRevealSpoiler: () -> Unit
) {
    val isSpoiler = review.spoilerPercent != null && currentProgress < review.spoilerPercent
    var revealed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (i <= review.rating) MaterialTheme.colorScheme.primary else Color(0xFFF0F0F0),
                        modifier = Modifier.width(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (isSpoiler && !revealed) {
                Text(
                    text = "Spoiler — read to ${(review.spoilerPercent!! * 100).toInt()}% to reveal",
                    color = Color(0xFF242424).copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .blur(16.dp)
                )
            } else {
                if (!review.reviewText.isNullOrBlank()) {
                    Text(
                        text = review.reviewText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                if (!review.whatILearned.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "What I Learned",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = review.whatILearned,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
