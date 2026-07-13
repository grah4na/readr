package com.readr.app.ui.share

import android.graphics.Bitmap
import android.graphics.Canvas
import com.readr.app.ui.share.templates.FinishedCardTemplate
import com.readr.app.ui.share.templates.QuoteCardTemplate
import com.readr.app.ui.share.templates.RatingCardTemplate

enum class CardType {
    QUOTE,
    RATING,
    FINISHED
}

data class CardData(
    val quoteText: String? = null,
    val bookTitle: String,
    val bookAuthor: String,
    val pageNumber: Int? = null,
    val rating: Int? = null,
    val coverBitmap: Bitmap? = null,
    val finishDate: String? = null,
    val totalPages: Int? = null,
    val whatILearnedSnippet: String? = null,
    val quoteSnippet: String? = null
)

object ShareCardRenderer {
    const val CANVAS_WIDTH = 1080
    const val CANVAS_HEIGHT = 1920

    fun renderCard(cardType: CardType, data: CardData): Bitmap {
        val bitmap = Bitmap.createBitmap(CANVAS_WIDTH, CANVAS_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        when (cardType) {
            CardType.QUOTE -> QuoteCardTemplate.render(canvas, data)
            CardType.RATING -> RatingCardTemplate.render(canvas, data)
            CardType.FINISHED -> FinishedCardTemplate.render(canvas, data)
        }

        return bitmap
    }

    fun renderFinishedCard(data: CardData): Bitmap {
        return renderCard(CardType.FINISHED, data)
    }
}
