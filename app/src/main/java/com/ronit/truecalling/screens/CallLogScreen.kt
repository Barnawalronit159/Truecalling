package com.ronit.truecalling.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ronit.truecalling.utils.formatDateTime
import com.ronit.truecalling.utils.formatDuration
import com.ronit.truecalling.viewmodel.CallLogViewModel

@Composable
fun CallLogScreen(viewModel: CallLogViewModel = viewModel()) {

    val context = LocalContext.current
    val logs by viewModel.logs.collectAsState()

    var hasCallLogPermission by remember { mutableStateOf(false) }
    var hasPhoneStatePermission by remember { mutableStateOf(false) }

    // 🔐 Permission launchers
    val callLogPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCallLogPermission = granted
        if (granted) {
            viewModel.loadLogs(context)
        }
    }

    val phoneStatePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPhoneStatePermission = granted
    }

    // 🔹 Initial permission check
    LaunchedEffect(Unit) {

        // CALL LOG permission
        val callLogCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALL_LOG
        )

        if (callLogCheck == PackageManager.PERMISSION_GRANTED) {
            hasCallLogPermission = true
            viewModel.loadLogs(context)
        } else {
            callLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
        }

        // PHONE STATE permission
        val phoneStateCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        )

        if (phoneStateCheck == PackageManager.PERMISSION_GRANTED) {
            hasPhoneStatePermission = true
        } else {
            phoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        }
    }

    //  Attach listener ONLY if permission granted
    if (hasPhoneStatePermission) {

        DisposableEffect(Unit) {

            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val listener = object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    if (state == TelephonyManager.CALL_STATE_IDLE) {
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            viewModel.loadLogs(context)
                        }, 2000) // 2 second delay
                    }
                }
            }

            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)

            onDispose {
                telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
            }
        }
    }

    //  UI
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(logs) { log ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        makeCall(context, log.number)
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                //  LEFT SIDE (Details)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = log.name ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = log.number,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = formatDateTime(log.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                //  RIGHT SIDE (Call Type + Duration)
                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = getCallEmoji(log.type),
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = getCallType(log.type),
                            color = getCallTypeColor(log.type),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (log.duration > 0) {
                        Text(
                            text = formatDuration(log.duration),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            // 🔻 Divider
            Divider(
                thickness = 0.5.dp,
                color = Color.LightGray
            )
        }
    }
}

// 🔹 Helper function
fun getCallType(type: Int): String {
    return when (type) {
        android.provider.CallLog.Calls.INCOMING_TYPE -> "Incoming"
        android.provider.CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
        android.provider.CallLog.Calls.MISSED_TYPE -> "Missed"
        else -> "Other"
    }
}

fun getCallEmoji(type: Int): String {
    return when (type) {
        CallLog.Calls.INCOMING_TYPE -> "📞⬇️"
        CallLog.Calls.OUTGOING_TYPE -> "📞⬆️"
        CallLog.Calls.MISSED_TYPE -> "📞❌"
        else -> "📞"
    }
}

fun getCallTypeColor(type: Int): Color {
    return when (type) {
        CallLog.Calls.INCOMING_TYPE -> Color(0xFF4CAF50) // green
        CallLog.Calls.OUTGOING_TYPE -> Color(0xFF2196F3) // blue
        CallLog.Calls.MISSED_TYPE -> Color.Red
        else -> Color.Gray
    }
}