package com.readr.app.ui.share.templates

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.readr.app.ui.share.CardData
import com.readr.app.ui.share.ShareCardRenderer

object FinishedCardTemplate {
    private val topBgPaint = Paint().apply { color = 0xFFFDFBF5.toInt() }
    private val bottomBgPaint = Paint().apply { color = 0xFF333333.toInt() }
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
    private val coverBorderPaint = Paint().apply {
        color = 0xFF4A6741.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }
    private val placeholderIconPaint = TextPaint().apply {
        color = 0xFFC7D4BF.toInt()
        alpha = 51
        textSize = 200f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
        textAlign = Paint.Align.CENTER
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
    private val learnedLabelPaint = TextPaint().apply {
        color = 0xFFFCC024.toInt()
        textSize = 32f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        letterSpacing = 0.15f
    }
    private val snippetPaint = TextPaint().apply {
        color = 0xFFFDFBF5.toInt()
        textSize = 40f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }
    private val noNotesPaint = TextPaint().apply {
        color = 0xFF777777.toInt()
        textSize = 36f
        isAntiAlias = true
        typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    }
    private val pagesPaint = TextPaint().apply {
        color = 0xFF7B9672.toInt()
        textSize = 36f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }
    private val readrPaint = TextPaint().apply {
        color = 0xFF777777.toInt()
        textSize = 28f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    fun render(canvas: Canvas, data: CardData) {
        val w = ShareCardRenderer.CANVAS_WIDTH.toFloat()
        val h = ShareCardRenderer.CANVAS_HEIGHT.toFloat()

        val splitY = h * 0.55f

        val topPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, splitY + 120f)
            quadTo(w * 0.5f, splitY, 0f, splitY - 80f)
            close()
        }
        canvas.drawPath(topPath, topBgPaint)

        val bottomPath = Path().apply {
            moveTo(0f, splitY - 80f)
            quadTo(w * 0.5f, splitY, w, splitY + 120f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        canvas.drawPath(bottomPath, bottomBgPaint)

        val curvePath = Path().apply {
            moveTo(0f, splitY - 80f)
            quadTo(w * 0.5f, splitY, w, splitY + 120f)
        }
        canvas.drawPath(curvePath, curveStrokePaint)

        canvas.drawText("FINISHED READING", 80f, 100f, finishedLabelPaint)

        val coverWidth = 440f
        val coverHeight = 660f
        val coverLeft = 80f
        val coverTop = 200f

        data.coverBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, coverLeft, coverTop, null)
            canvas.drawRoundRect(
                RectF(coverLeft, coverTop, coverLeft + coverWidth, coverTop + coverHeight),
                8f, 8f, coverBorderPaint
            )
        } ?: run {
            canvas.drawText("\uD83D\uDCD6", w / 2f, coverTop + coverHeight / 2f + 70f, placeholderIconPaint)
        }

        canvas.drawText(data.bookTitle, 80f, 920f, titlePaint)
        canvas.drawText(data.bookAuthor, 80f, 1020f, authorPaint)

        data.finishDate?.let { date ->
            canvas.drawText("Completed on $date", 80f, 1080f, datePaint)
        }

        canvas.drawText("WHAT I LEARNED", 80f, 1180f, learnedLabelPaint)

        val snippet = data.whatILearnedSnippet
        if (!snippet.isNullOrBlank()) {
            val snippetLayout = StaticLayout.Builder.obtain(
                snippet, 0, snippet.length, snippetPaint, (w - 160f).toInt()
            )
                .setLineSpacing(0f, 1.4f)
                .setMaxLines(4)
                .setEllipsize(android.text.TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
            canvas.save()
            canvas.translate(80f, 1240f)
            snippetLayout.draw(canvas)
            canvas.restore()
        } else {
            canvas.drawText(
                "No notes yet \u2014 but the journey was enough.",
                80f, 1290f, noNotesPaint
            )
        }

        data.totalPages?.let { pages ->
            canvas.drawText("$pages pages", 80f, 1720f, pagesPaint)
        }

        canvas.drawText("Readr", w - 160f, 1860f, readrPaint)
    }
}
