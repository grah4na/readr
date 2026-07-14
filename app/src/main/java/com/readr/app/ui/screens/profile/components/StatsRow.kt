package com.readr.app.ui.screens.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.data.model.ProfileStats
import com.readr.app.ui.theme.DarkCharcoal
import com.readr.app.ui.theme.LightGrey
import com.readr.app.ui.theme.MediumGrey
import com.readr.app.ui.theme.OffWhite
import com.readr.app.ui.theme.PrimaryYellow
import com.readr.app.ui.theme.LightSage
import com.readr.app.ui.theme.SageGreen

@Composable
fun StatsRow(stats: ProfileStats, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                label = "Pages Read",
                value = formatNumber(stats.pagesRead),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Hours Spent",
                value = formatNumber(stats.hoursSpent),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                label = "Avg Rating",
                value = "${"%.1f".format(stats.avgRating)} ★",
                valueColor = PrimaryYellow,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Books Finished",
                value = formatNumber(stats.booksFinished),
                modifier = Modifier.weight(1f)
            )
        }
        if (stats.longestBookTitle != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = LightSage),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Longest read: ${stats.longestBookTitle} (${stats.longestBookPages} pages)",
                    style = MaterialTheme.typography.bodySmall,
                    color = SageGreen,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
        if (stats.booksReading > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = LightSage),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Currently reading: ${stats.booksReading} book${if (stats.booksReading != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SageGreen,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: androidx.compose.ui.graphics.Color = DarkCharcoal
) {
    Card(
        modifier = modifier.border(1.dp, LightGrey, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = OffWhite),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = MediumGrey,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatNumber(n: Int): String {
    return when {
        n >= 1_000_000 -> "${"%.1f".format(n / 1_000_000.0)}M"
        n >= 1_000 -> "${"%.1f".format(n / 1_000.0)}K"
        else -> n.toString()
    }
}
