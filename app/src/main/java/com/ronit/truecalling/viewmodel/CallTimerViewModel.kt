package com.ronit.truecalling.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.system.*

class CallTimerViewModel : ViewModel() {

    private val _callDuration = MutableStateFlow(0L)
    val callDuration: StateFlow<Long> = _callDuration

    fun loadLatestCallDuration(context: Context) {

        val cursor = context.contentResolver.query(
            android.provider.CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            android.provider.CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {

            if (it.moveToFirst()) {

                val durationIndex =
                    it.getColumnIndex(android.provider.CallLog.Calls.DURATION)

                val duration = it.getLong(durationIndex)

                _callDuration.value = duration * 1000 // convert to ms
            }
        }
    }

    fun reset() {
        _callDuration.value = 0
    }
}