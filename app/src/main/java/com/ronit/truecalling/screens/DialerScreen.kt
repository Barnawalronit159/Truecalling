package com.ronit.truecalling.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.ronit.truecalling.components.DialButton

@Composable
fun DialerScreen() {

    var number by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && number.isNotBlank()) {
            makeCall(context, number)
        }
    }

    val buttons = listOf(
        "1","2","3",
        "4","5","6",
        "7","8","9",
        "*","0","#"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(150.dp))

        // 🔹 Number + Backspace
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = if (number.isEmpty()) "Enter number" else number,
                fontSize = 34.sp,
                color = if (number.isEmpty()) Color.Gray else Color.White,
                modifier = Modifier.weight(1f)
            )

            if (number.isNotEmpty()) {
                IconButton(
                    onClick = { number = number.dropLast(1) }
                ) {
                    Icon(Icons.Default.Backspace, contentDescription = "Delete")
                }
            }
        }

        //  Dial Pad
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(buttons) { digit ->
                DialButton(
                    digit = digit,
                    onClick = { number += digit }
                )
            }
        }

        //  Call Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .clickable {

                        if (number.isBlank()) return@clickable

                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        )

                        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                            makeCall(context, number)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color.White
                )
            }
        }
    }
}

//  Call function
fun makeCall(context: android.content.Context, number: String) {
    val intent = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse("tel:$number")
    }
    context.startActivity(intent)
}