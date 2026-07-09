package com.readr.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.readr.app.model.Book

@Composable
fun HomeScreen() {
    val currentlyReading = Book(
        "Atomic Habits",
        "James Clear",
        "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg",
        0.65f
    )

    val popularBooks = listOf(
        Book("The Alchemist", "Paulo Coelho", "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg"),
        Book("1984", "George Orwell", "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg"),
        Book("Deep Work", "Cal Newport", "https://covers.openlibrary.org/b/isbn/9781455586691-L.jpg"),
        Book("The Great Gatsby", "F. Scott Fitzgerald", "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg")
    )

    val fictionBooks = listOf(
        Book("The Hobbit", "J.R.R. Tolkien", "https://covers.openlibrary.org/b/isbn/9780547928227-L.jpg"),
        Book("Harry Potter", "J.K. Rowling", "https://covers.openlibrary.org/b/isbn/9780545582889-L.jpg"),
        Book("Dune", "Frank Herbert", "https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Currently Reading",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            CurrentlyReadingCard(currentlyReading)
        }

        item {
            SectionHeader("Popular This Week")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(popularBooks) { book ->
                    BookItem(book)
                }
            }
        }

        item {
            SectionHeader("Fiction")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(fictionBooks) { book ->
                    BookItem(book)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun CurrentlyReadingCard(book: Book) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = book.progress,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${(book.progress * 100).toInt()}% finished",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun BookItem(book: Book) {
    Column(modifier = Modifier.width(120.dp)) {
        AsyncImage(
            model = book.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp, 180.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.author,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
