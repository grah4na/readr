package com.readr.app.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readr.app.data.local.entity.UserProfileEntity
import com.readr.app.ui.theme.DarkCharcoal
import com.readr.app.ui.theme.DarkGreen
import com.readr.app.ui.theme.MediumGrey
import com.readr.app.ui.theme.PrimaryYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBottomSheet(
    profile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, bio: String, pronouns: String) -> Unit
) {
    var displayName by remember(profile) { mutableStateOf(profile?.displayName ?: "") }
    var bio by remember(profile) { mutableStateOf(profile?.bio ?: "") }
    var pronouns by remember(profile) { mutableStateOf(profile?.pronouns ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkCharcoal
            )

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    cursorColor = DarkGreen
                )
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { if (it.length <= 160) bio = it },
                label = { Text("Bio (max 160 characters)") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 4,
                supportingText = { Text("${bio.length}/160", color = MediumGrey, fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    cursorColor = DarkGreen
                )
            )

            OutlinedTextField(
                value = pronouns,
                onValueChange = { if (it.length <= 20) pronouns = it },
                label = { Text("Pronouns (max 20 characters)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = { Text("${pronouns.length}/20", color = MediumGrey, fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onSave(displayName, bio, pronouns) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryYellow,
                    contentColor = DarkCharcoal
                )
            ) {
                Text(text = "Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
