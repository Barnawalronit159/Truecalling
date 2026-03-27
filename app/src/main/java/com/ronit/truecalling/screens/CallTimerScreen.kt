package com.ronit.truecalling.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronit.truecalling.viewmodel.CallTimerViewModel

@Composable
fun CallTimerScreen(viewModel: CallTimerViewModel = viewModel()) {

    val context = LocalContext.current
    val duration by viewModel.callDuration.collectAsState()

    var hasPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true
        } else {
            permissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        }
    }

    if (hasPermission) {
        DisposableEffect(Unit) {

            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val listener = object : PhoneStateListener() {

                override fun onCallStateChanged(state: Int, phoneNumber: String?) {

                    if (state == TelephonyManager.CALL_STATE_IDLE) {
                        viewModel.loadLatestCallDuration(context)
                    }
                }
            }

            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)

            onDispose {
                telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Last Call Duration",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = formatDuration(duration),
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}


fun formatDuration(ms: Long): String {
    val seconds = ms / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return String.format("%02d:%02d", minutes, remainingSeconds)
}