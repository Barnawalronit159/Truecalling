package com.ronit.truecalling.screens

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronit.truecalling.viewmodel.ContactViewModel

@Composable
fun ContactsScreen(viewModel: ContactViewModel = viewModel()) {

    val context = LocalContext.current
    val contacts by viewModel.contacts.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.loadContacts(context)
        }
    }

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadContacts(context)
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        item {
            Text(
                text = "Contacts",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(contacts) { contact ->

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            makeCall(context, contact.phoneNumber)
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔵 Avatar
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3F51B5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contact.name.first().uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    //  Name + Number
                    Column {

                        Text(
                            text = contact.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = contact.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                //  Divider
                Divider(
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}