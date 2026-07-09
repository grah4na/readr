package com.readr.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GlassBackground = Color(0xBB242424)
private val GlassBorderStart = Color(0x66FFFFFF)
private val GlassBorderEnd = Color(0x00000000)
private val GlareStart = Color(0x18FFFFFF)
private val GlareEnd = Color(0x00000000)

private val PillShape = RoundedCornerShape(28.dp)
private val BarHeight = 72.dp

@Composable
fun LiquidGlassBottomNav(
    items: List<LiquidGlassNavItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var barSize by remember { mutableStateOf(IntSize.Zero) }

    val tabCount = items.size.coerceAtLeast(1)
    val tabWidthPx = if (barSize.width > 0) barSize.width.toFloat() / tabCount else 0f
    val pillW = tabWidthPx * 0.50f
    val pillStart = (tabWidthPx - pillW) * 0.5f
    val targetOffset = if (tabWidthPx > 0f) selectedIndex * tabWidthPx + pillStart else 0f

    val animatedOffset by animateFloatAsState(
        targetValue = targetOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pillOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BarHeight)
            .onSizeChanged { barSize = it }
            .background(color = GlassBackground, shape = PillShape)
            .drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(GlassBorderStart, GlassBorderEnd),
                        start = Offset(size.width * 0.3f, 0f),
                        end = Offset(size.width, size.height)
                    ),
                    style = Stroke(width = 1.dp.toPx()),
                    cornerRadius = CornerRadius(28.dp.toPx()),
                    size = Size(size.width, size.height)
                )
            }
            .clip(PillShape)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(GlareStart, GlareEnd),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height * 0.4f)
                ),
                cornerRadius = CornerRadius(28.dp.toPx()),
                size = size
            )
        }

        Box(
            modifier = Modifier
                .offset(x = with(density) { animatedOffset.toDp() })
                .padding(vertical = 6.dp)
                .size(
                    width = with(density) { pillW.toDp() },
                    height = BarHeight - 12.dp
                )
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(BarHeight),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                NavBarTab(
                    item = item,
                    isSelected = index == selectedIndex,
                    onClick = { onItemClick(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

data class LiquidGlassNavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@Composable
private fun RowScope.NavBarTab(
    item: LiquidGlassNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = MaterialTheme.colorScheme.primary
    val contentAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.55f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "contentAlpha"
    )
    val tint = if (isSelected) accent else Color.White.copy(alpha = contentAlpha)
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconScale"
    )

    Box(
        modifier = modifier
            .height(BarHeight)
            .clip(RectangleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = tint,
                modifier = Modifier.size(24.dp * iconScale)
            )
            Text(
                text = item.label,
                color = tint,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
