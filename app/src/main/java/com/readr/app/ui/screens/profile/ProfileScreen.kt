package com.readr.app.ui.screens.profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.readr.app.data.local.entity.UserProfileEntity
import com.readr.app.data.model.ReadingEntry
import com.readr.app.ui.screens.profile.components.BookListItem
import com.readr.app.ui.screens.profile.components.EditProfileBottomSheet
import com.readr.app.ui.screens.profile.components.StatsRow
import com.readr.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val tabs = listOf("Profile", "Diary", "Want to Read")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateToSettings: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val finishedBooks by viewModel.finishedBooks.collectAsState()
    val wantToReadBooks by viewModel.wantToReadBooks.collectAsState()
    val currentlyReading by viewModel.currentlyReading.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfilePhoto(it) }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> ProfileTab(
                    userProfile = userProfile,
                    stats = stats,
                    currentlyReading = currentlyReading,
                    isEditing = isEditing,
                    onEditProfile = { viewModel.showEditProfile() },
                    onDismissEdit = { viewModel.hideEditProfile() },
                    onSaveProfile = { name, bio, pronouns -> viewModel.updateProfile(name, bio, pronouns) },
                    onPickPhoto = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    onNavigateToSettings = onNavigateToSettings
                )
                1 -> BooksListTab(
                    books = finishedBooks,
                    emptyTitle = "No finished books yet",
                    emptySubtitle = "Finish your first read to see it here.",
                    onStartReading = null
                )
                2 -> BooksListTab(
                    books = wantToReadBooks,
                    emptyTitle = "Your reading list is empty",
                    emptySubtitle = "Tap + to add books.",
                    onStartReading = { entry -> viewModel.startReading(entry.id) }
                )
            }
        }
    }
}

@Composable
private fun ProfileTab(
    userProfile: UserProfileEntity?,
    stats: com.readr.app.data.model.ProfileStats,
    currentlyReading: List<ReadingEntry>,
    isEditing: Boolean,
    onEditProfile: () -> Unit,
    onDismissEdit: () -> Unit,
    onSaveProfile: (String, String, String) -> Unit,
    onPickPhoto: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            ProfileHeader(
                userProfile = userProfile,
                onPickPhoto = onPickPhoto,
                onEditProfile = onEditProfile,
                onNavigateToSettings = onNavigateToSettings
            )
        }

        if (currentlyReading.isNotEmpty()) {
            item {
                CurrentlyReadingSection(
                    books = currentlyReading,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsRow(stats = stats)
            }
        }
    }

    if (isEditing) {
        EditProfileBottomSheet(
            profile = userProfile,
            onDismiss = onDismissEdit,
            onSave = onSaveProfile
        )
    }
}

@Composable
private fun ProfileHeader(
    userProfile: UserProfileEntity?,
    onPickPhoto: () -> Unit,
    onEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { onPickPhoto() },
            contentAlignment = Alignment.Center
        ) {
            val photoUri = userProfile?.profilePhotoUri
            if (photoUri != null) {
                AsyncImage(
                    model = photoUri.toUri(),
                    contentDescription = "Profile photo",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (userProfile?.displayName?.firstOrNull()?.uppercase() ?: "?"),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userProfile?.displayName ?: "Reader",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        userProfile?.pronouns?.let { pronouns ->
            if (pronouns.isNotBlank()) {
                Text(
                    text = pronouns,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        userProfile?.bio?.let { bio ->
            if (bio.isNotBlank()) {
                Text(
                    text = bio,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Edit Profile",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onEditProfile() }
        )
    }
}

@Composable
private fun BooksListTab(
    books: List<ReadingEntry>,
    emptyTitle: String,
    emptySubtitle: String,
    onStartReading: ((ReadingEntry) -> Unit)?
) {
    if (books.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📖",
                    fontSize = 28.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = emptyTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = emptySubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(books, key = { it.id }) { entry ->
                BookListItem(
                    entry = entry,
                    onStartReading = if (onStartReading != null) {
                        { onStartReading(entry) }
                    } else null
                )
            }
        }
    }
}

@Composable
private fun CurrentlyReadingSection(
    books: List<ReadingEntry>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Currently Reading",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        books.forEach { entry ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AsyncImage(
                        model = if (entry.coverUrl.isNotBlank()) entry.coverUrl else null,
                        contentDescription = entry.title,
                        modifier = Modifier
                            .width(50.dp)
                            .height(75.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = entry.author,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (entry.dateStarted > 0) {
                            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                            Text(
                                text = "Started: ${dateFormat.format(Date(entry.dateStarted))}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
