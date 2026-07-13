package com.readr.app.ui.share.templates

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.readr.app.ui.share.CardData
import com.readr.app.ui.share.ShareCardRenderer
import kotlin.math.cos
import kotlin.math.sin

object FinishedCardTemplate {
    private val topBgPaint = Paint().apply {
        color = 0xFFFDFBF5.toInt()
    }
    private val bottomBgPaint = Paint().apply {
        color = 0xFF333333.toInt()
    }
    private val curveStrokePaint = Paint().apply {
        color = 0xFFFCC024.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }
    private val finishedLabelPaint = TextPaint().apply {
        color = 0xFF4A6741.toInt()
        textSize = 48f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        letterSpacing = 0.17f
    }
    private val coverShadowPaint = Paint().apply {
        color = 0x33000000.toInt()
    }
    private val coverBorderPaint = Paint().apply {
        color = 0xFF4A6741.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }
    private val placeholderBgPaint = Paint().apply {
        color = 0xFFC7D4BF.toInt()
        alpha = 77
        isAntiAlias = true
    }
    private val placeholderIconPaint = Paint().apply {
        color = 0xFF7B9672.toInt()
        alpha = 128
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val titlePaint = TextPaint().apply {
        color = 0xFF333333.toInt()
        textSize = 68f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    private val authorPaint = TextPaint().apply {
        color = 0xFF777777.toInt()
        textSize = 44f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }
    private val datePaint = TextPaint().apply {
        color = 0xFF7B9672.toInt()
        textSize = 36f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }
    private val starFilledPaint = Paint().apply {
        color = 0xFFFCC024.toInt()
        isAntiAlias = true
    }
    private val starEmptyPaint = Paint().apply {
        color = 0xFF7B9672.toInt()
        alpha = 77
        isAntiAlias = true
    }
    private val learnedLabelPaint = TextPaint().apply {
        color = 0xFFFCC024.toInt()
        textSize = 32f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        letterSpacing = 0.12f
    }
    private val snippetPaint = TextPaint().apply {
        color = 0xFFFDFBF5.toInt()
        textSize = 40f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }
    private val snippetQuotePaint = TextPaint().apply {
        color = 0xFFFDFBF5.toInt()
        textSize = 40f
        isAntiAlias = true
        typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    }
    private val noNotesPaint = TextPaint().apply {
        color = 0xFF777777.toInt()
        textSize = 36f
        isAntiAlias = true
        typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    }
    private val readrPaint = TextPaint().apply {
        color = 0xFF777777.toInt()
        alpha = 153
        textSize = 32f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
        textAlign = Paint.Align.RIGHT
    }

    fun render(canvas: Canvas, data: CardData) {
        val w = ShareCardRenderer.CANVAS_WIDTH.toFloat()
        val h = ShareCardRenderer.CANVAS_HEIGHT.toFloat()

        val splitLeftY = 1050f
        val splitRightY = 1120f
        val splitControlY = 1180f
        val splitControlX = 540f

        val topPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, splitRightY + 40f)
            quadTo(splitControlX, splitControlY, 0f, splitLeftY - 20f)
            close()
        }
        canvas.drawPath(topPath, topBgPaint)

        val bottomPath = Path().apply {
            moveTo(0f, splitLeftY - 20f)
            quadTo(splitControlX, splitControlY, w, splitRightY + 40f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        canvas.drawPath(bottomPath, bottomBgPaint)

        val curvePath = Path().apply {
            moveTo(0f, splitLeftY)
            quadTo(splitControlX, splitControlY, w, splitRightY)
        }
        canvas.drawPath(curvePath, curveStrokePaint)

        canvas.drawText("FINISHED READING", 80f, 100f, finishedLabelPaint)

        val coverLeft = 80f
        val coverTop = 200f
        val coverWidth = 440f
        val coverHeight = 660f

        data.coverBitmap?.let { bitmap ->
            val srcW = bitmap.width.toFloat()
            val srcH = bitmap.height.toFloat()
            val scale = maxOf(coverWidth / srcW, coverHeight / srcH)
            val scaledW = srcW * scale
            val scaledH = srcH * scale
            val offsetX = coverLeft + (coverWidth - scaledW) / 2f
            val offsetY = coverTop + (coverHeight - scaledH) / 2f
            val matrix = Matrix().apply {
                postScale(scale, scale)
                postTranslate(offsetX, offsetY)
            }
            canvas.drawRoundRect(
                RectF(coverLeft + 8f, coverTop + 8f, coverLeft + coverWidth + 8f, coverTop + coverHeight + 8f),
                8f, 8f, coverShadowPaint
            )
            canvas.save()
            canvas.clipRect(coverLeft, coverTop, coverLeft + coverWidth, coverTop + coverHeight)
            canvas.drawBitmap(bitmap, matrix, null)
            canvas.restore()
            canvas.drawRoundRect(
                RectF(coverLeft, coverTop, coverLeft + coverWidth, coverTop + coverHeight),
                4f, 4f, coverBorderPaint
            )
        } ?: run {
            val placeholderRect = RectF(coverLeft, coverTop, coverLeft + coverWidth, coverTop + coverHeight)
            canvas.drawRoundRect(placeholderRect, 8f, 8f, placeholderBgPaint)
            val iconCx = coverLeft + coverWidth / 2f
            val iconCy = coverTop + coverHeight / 2f
            drawPlaceholderBookIcon(canvas, iconCx, iconCy, placeholderIconPaint)
        }

        val maxTextWidth = (w - 160f).toInt()
        val titleTop = 920f
        val titleLayout = StaticLayout.Builder.obtain(
            data.bookTitle, 0, data.bookTitle.length, titlePaint, maxTextWidth
        )
            .setLineSpacing(0f, 1.15f)
            .setMaxLines(2)
            .setEllipsize(android.text.TextUtils.TruncateAt.END)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .build()

        canvas.save()
        canvas.translate(80f, titleTop)
        titleLayout.draw(canvas)
        canvas.restore()

        val titleHeight = titleLayout.height.toFloat()
        val authorTop = titleTop + titleHeight + 20f
        canvas.drawText(data.bookAuthor, 80f, authorTop, authorPaint)

        val rating = data.rating
        if (rating != null && rating in 1..5) {
            val starSize = 56f
            val starGap = 12f
            val starStartX = 80f
            val starCenterY = 1250f
            for (i in 0 until 5) {
                val cx = starStartX + i * (starSize + starGap) + starSize / 2f
                val paint = if (i < rating) starFilledPaint else starEmptyPaint
                drawStar(canvas, cx, starCenterY, starSize / 2f, paint)
            }
        }

        val starsPresent = rating != null && rating in 1..5
        val learnedLabelTop = if (starsPresent) 1330f else 1250f
        canvas.drawText("WHAT I LEARNED", 80f, learnedLabelTop, learnedLabelPaint)

        val snippetTop = learnedLabelTop + 60f
        val snippetText = if (!data.whatILearnedSnippet.isNullOrBlank()) {
            data.whatILearnedSnippet
        } else if (!data.quoteSnippet.isNullOrBlank()) {
            "\u2014 ${data.quoteSnippet}"
        } else if (!data.quoteText.isNullOrBlank()) {
            "\u2014 ${data.quoteText}"
        } else {
            null
        }

        if (snippetText != null) {
            val paint = if (!data.whatILearnedSnippet.isNullOrBlank()) snippetPaint else snippetQuotePaint
            val snippetLayout = StaticLayout.Builder.obtain(
                snippetText, 0, snippetText.length, paint, maxTextWidth
            )
                .setLineSpacing(0f, 1.35f)
                .setMaxLines(4)
                .setEllipsize(android.text.TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
            canvas.save()
            canvas.translate(80f, snippetTop)
            snippetLayout.draw(canvas)
            canvas.restore()
        } else {
            canvas.drawText(
                "Every book leaves a mark. This one was yours.",
                80f, snippetTop + 10f, noNotesPaint
            )
        }

        canvas.drawText("Readr", w - 80f, 1860f, readrPaint)
    }

    private fun drawPlaceholderBookIcon(canvas: Canvas, cx: Float, cy: Float, paint: Paint) {
        val pw = 100f
        val ph = 140f
        val corner = 12f
        val spineOffset = 6f

        val leftPage = Path().apply {
            moveTo(cx - spineOffset, cy - ph / 2f)
            lineTo(cx - pw, cy - ph / 2f + corner)
            quadTo(cx - pw - 8f, cy - ph / 2f + corner + 4f, cx - pw - 4f, cy - ph / 2f + corner + 20f)
            lineTo(cx - pw - 4f, cy + ph / 2f - corner)
            quadTo(cx - pw - 4f, cy + ph / 2f, cx - pw, cy + ph / 2f)
            lineTo(cx - spineOffset, cy + ph / 2f - 4f)
            close()
        }
        canvas.drawPath(leftPage, paint)

        val rightPage = Path().apply {
            moveTo(cx + spineOffset, cy - ph / 2f)
            lineTo(cx + pw, cy - ph / 2f + corner)
            quadTo(cx + pw + 8f, cy - ph / 2f + corner + 4f, cx + pw + 4f, cy - ph / 2f + corner + 20f)
            lineTo(cx + pw + 4f, cy + ph / 2f - corner)
            quadTo(cx + pw + 4f, cy + ph / 2f, cx + pw, cy + ph / 2f)
            lineTo(cx + spineOffset, cy + ph / 2f - 4f)
            close()
        }
        canvas.drawPath(rightPage, paint)

        val spinePaint = Paint(paint).apply {
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }
        canvas.drawLine(cx, cy - ph / 2f + 4f, cx, cy + ph / 2f - 8f, spinePaint)
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
