package com.readr.app.ui.share.templates

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.readr.app.ui.share.CardData
import com.readr.app.ui.share.ShareCardRenderer

object QuoteCardTemplate {
    private val backgroundColor = Paint().apply { color = 0xFFF5F1E9.toInt() }
    private val accentBarPaint = Paint().apply { color = 0xFFFCC024.toInt() }
    private val marginStripPaint = Paint().apply { color = 0xFFC7D4BF.toInt() }
    private val wordmarkPaint = TextPaint().apply {
        color = 0xFF777777.toInt()
        textSize = 28f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }
    private val quoteMarkPaint = Paint().apply {
        color = 0xFF7B9672.toInt()
        alpha = 64
        isAntiAlias = true
        textSize = 180f
        typeface = Typeface.DEFAULT
    }
    private val dividerPaint = Paint().apply {
        color = 0xFFE0E0E0.toInt()
        strokeWidth = 1f
    }
    private val bookIconPaint = Paint().apply {
        color = 0xFFC7D4BF.toInt()
        isAntiAlias = true
        textSize = 48f
        typeface = Typeface.DEFAULT
    }
    private val pageLabelPaint = TextPaint().apply {
        color = 0xFF7B9672.toInt()
        textSize = 36f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    fun render(canvas: Canvas, data: CardData) {
        val w = ShareCardRenderer.CANVAS_WIDTH.toFloat()
        val h = ShareCardRenderer.CANVAS_HEIGHT.toFloat()

        canvas.drawRect(0f, 0f, w, h, backgroundColor)

        canvas.drawRect(0f, 60f, w, 76f, accentBarPaint)

        val stripX = 80f
        canvas.drawRect(stripX, 200f, stripX + 12f, 1700f, marginStripPaint)

        canvas.drawText("Readr", 80f, 80f, wordmarkPaint)

        val quoteText = data.quoteText ?: ""

        canvas.drawText("\u201C", 80f, 440f, quoteMarkPaint)

        val charCount = quoteText.length
        val textSize = when {
            charCount < 120 -> 80f
            charCount > 350 -> 52f
            else -> 64f
        }

        val quotePaint = TextPaint().apply {
            this.textSize = textSize
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
            color = 0xFF333333.toInt()
        }

        val textX = 140f
        val maxWidth = 860f
        val quoteLayout = StaticLayout.Builder.obtain(
            quoteText, 0, quoteText.length, quotePaint, maxWidth.toInt()
        )
            .setLineSpacing(0f, 1.5f)
            .setMaxLines(8)
            .setEllipsize(android.text.TextUtils.TruncateAt.END)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .build()

        canvas.save()
        canvas.translate(textX, 520f)
        quoteLayout.draw(canvas)
        canvas.restore()

        val dividerY = 1500f
        canvas.drawRect(80f, dividerY, w - 80f, dividerY + 1f, dividerPaint)

        val bottomTextY = 1580f
        val titlePaint = TextPaint().apply {
            color = 0xFF333333.toInt()
            textSize = 56f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }
        val authorPaint = TextPaint().apply {
            color = 0xFF777777.toInt()
            textSize = 44f
            isAntiAlias = true
            typeface = Typeface.DEFAULT
        }

        canvas.drawText(data.bookTitle, 140f, bottomTextY, titlePaint)
        canvas.drawText("by ${data.bookAuthor}", 140f, bottomTextY + 70f, authorPaint)

        data.pageNumber?.let { page ->
            val pageText = "\u2014 p. $page"
            val pageWidth = pageLabelPaint.measureText(pageText)
            canvas.drawText(pageText, w - 80f - pageWidth, bottomTextY, pageLabelPaint)
        }

        canvas.drawText("\uD83D\uDCD6", 980f, 1840f, bookIconPaint)
    }
}
