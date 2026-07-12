package com.readr.app.ui.share.templates

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import com.readr.app.ui.share.CardData
import com.readr.app.ui.share.ShareCardRenderer
import kotlin.math.cos
import kotlin.math.sin

object RatingCardTemplate {
    private val bgPaint = Paint().apply { color = 0xFF4A6741.toInt() }
    private val ratedLabelPaint = TextPaint().apply {
        color = 0xFFFDFBF5.toInt()
        textSize = 44f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        letterSpacing = 0.2f
    }
    private val starFilledPaint = Paint().apply {
        color = 0xFFFCC024.toInt()
        isAntiAlias = true
    }
    private val starEmptyPaint = Paint().apply {
        color = 0xFF7B9672.toInt()
        alpha = 102
        isAntiAlias = true
    }
    private val ratingNumberPaint = TextPaint().apply {
        color = 0xFFFCC024.toInt()
        textSize = 72f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    private val borderPaint = Paint().apply {
        color = 0xFFFDFBF5.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }
    private val shadowPaint = Paint().apply {
        color = 0x33000000.toInt()
    }
    private val placeholderBgPaint = Paint().apply {
        color = 0xFF7B9672.toInt()
        alpha = 77
        isAntiAlias = true
    }
    private val placeholderIconPaint = TextPaint().apply {
        color = 0xFFFDFBF5.toInt()
        textSize = 120f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
        textAlign = Paint.Align.CENTER
    }
    private val titlePaint = TextPaint().apply {
        color = 0xFFFDFBF5.toInt()
        textSize = 64f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    private val authorPaint = TextPaint().apply {
        color = 0xFFC7D4BF.toInt()
        textSize = 44f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
        textAlign = Paint.Align.CENTER
    }
    private val readrPaint = TextPaint().apply {
        color = 0xFF7B9672.toInt()
        textSize = 28f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
        textAlign = Paint.Align.CENTER
    }

    fun render(canvas: Canvas, data: CardData) {
        val w = ShareCardRenderer.CANVAS_WIDTH.toFloat()
        val h = ShareCardRenderer.CANVAS_HEIGHT.toFloat()

        canvas.drawRect(0f, 0f, w, h, bgPaint)

        val ratedY = h * 0.12f
        canvas.drawText("RATED", w / 2f, ratedY, ratedLabelPaint)

        val starCenterY = h * 0.25f
        val starRadius = 50f
        val starGap = 20f
        val totalStarsWidth = 5 * starRadius * 2 + 4 * starGap
        val starStartX = (w - totalStarsWidth) / 2 + starRadius

        val rating = (data.rating ?: 0).coerceIn(1, 5)

        if (rating == 5) {
            val glowPaint = Paint().apply {
                color = 0x26FCC024.toInt()
                isAntiAlias = true
            }
            val glowCenterX = starStartX + 2 * (starRadius * 2 + starGap)
            canvas.drawCircle(glowCenterX, starCenterY, starRadius * 3f, glowPaint)
        }

        for (i in 0 until 5) {
            val cx = starStartX + i * (starRadius * 2 + starGap)
            val paint = if (i < rating) starFilledPaint else starEmptyPaint
            drawStar(canvas, cx, starCenterY, starRadius, paint)
        }

        val ratingText = "$rating/5"
        canvas.drawText(ratingText, w / 2f, starCenterY + 120f, ratingNumberPaint)

        val coverCenterY = h * 0.5f
        val coverWidth = 560f
        val coverHeight = 840f
        val coverLeft = (w - coverWidth) / 2f
        val coverTop = coverCenterY - coverHeight / 2f

        data.coverBitmap?.let { bitmap ->
            canvas.drawRoundRect(
                RectF(coverLeft + 8f, coverTop + 8f, coverLeft + coverWidth + 8f, coverTop + coverHeight + 8f),
                16f, 16f, shadowPaint
            )
            canvas.drawBitmap(bitmap, coverLeft, coverTop, null)
            canvas.drawRoundRect(
                RectF(coverLeft, coverTop, coverLeft + coverWidth, coverTop + coverHeight),
                12f, 12f, borderPaint
            )
        } ?: run {
            val placeholderRect = RectF(coverLeft, coverTop, coverLeft + coverWidth, coverTop + coverHeight)
            canvas.drawRoundRect(placeholderRect, 24f, 24f, placeholderBgPaint)
            canvas.drawText("\uD83D\uDCDA", w / 2f, coverTop + coverHeight / 2f + 40f, placeholderIconPaint)
        }

        val bottomY = h * 0.77f
        val maxTitleWidth = 900f
        val title = data.bookTitle
        titlePaint.textSize = 64f
        val titleLines = mutableListOf<String>()
        val words = title.split(" ")
        var currentLine = ""
        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (titlePaint.measureText(testLine) <= maxTitleWidth) {
                currentLine = testLine
            } else {
                titleLines.add(currentLine)
                currentLine = word
            }
        }
        if (currentLine.isNotEmpty()) titleLines.add(currentLine)
        val displayLines = titleLines.take(2)
        var lineY = bottomY - (displayLines.size - 1) * 40f
        for (line in displayLines) {
            canvas.drawText(line, w / 2f, lineY, titlePaint)
            lineY += 80f
        }

        val authorLineY = if (displayLines.size == 1) bottomY + 80f else bottomY + 120f
        canvas.drawText(data.bookAuthor, w / 2f, authorLineY, authorPaint)

        canvas.drawText("Readr", w / 2f, h - 60f, readrPaint)
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, radius: Float, paint: Paint) {
        val path = Path()
        val outerR = radius
        val innerR = radius * 0.4f
        val step = Math.PI / 5.0
        path.moveTo(
            (cx + outerR * cos(-Math.PI / 2)).toFloat(),
            (cy + outerR * sin(-Math.PI / 2)).toFloat()
        )
        for (i in 1 until 10) {
            val r = if (i % 2 == 0) outerR else innerR
            val angle = -Math.PI / 2 + i * step
            path.lineTo(
                (cx + r * cos(angle)).toFloat(),
                (cy + r * sin(angle)).toFloat()
            )
        }
        path.close()
        canvas.drawPath(path, paint)
    }
}
