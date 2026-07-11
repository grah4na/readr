package com.readr.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.navigation.Screen
import com.readr.app.ui.theme.DarkCharcoal
import com.readr.app.ui.theme.MediumGrey
import com.readr.app.ui.theme.PrimaryYellow

@Composable
fun ReadrBottomNavBar(
    items: List<Screen>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(DarkCharcoal)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, screen ->
            val isSelected = index == selectedIndex
            val contentColor = if (isSelected) PrimaryYellow else MediumGrey

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onItemClick(index) }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.title,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                if (screen.title.isNotEmpty()) {
                    Text(
                        text = screen.title,
                        color = contentColor,
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
