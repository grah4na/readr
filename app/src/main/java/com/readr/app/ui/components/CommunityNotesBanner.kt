package com.readr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.data.local.entity.CommunityNoteEntity

@Composable
fun CommunityNotesBanner(communityNotes: List<CommunityNoteEntity>) {
    if (communityNotes.isEmpty()) return

    val summary = communityNotes.joinToString(", ") { note ->
        "[${note.type}] ${note.noteText.take(60)}"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFCC024),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = "Community notes",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 14.sp
            )
        }
        Text(
            text = summary,
            color = Color.Black,
            fontSize = 13.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
