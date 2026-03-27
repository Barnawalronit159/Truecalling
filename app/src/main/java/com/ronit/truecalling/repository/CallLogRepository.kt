package com.ronit.truecalling.repository

import android.content.Context
import android.provider.CallLog
import com.ronit.truecalling.model.CallLogItem

class CallLogRepository {

    fun getCallLogs(context: Context): List<CallLogItem> {

        val callList = mutableListOf<CallLogItem>()


        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {

            val nameIndex = it.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)
            val numberIndex = it.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndexOrThrow(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndexOrThrow(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndexOrThrow(CallLog.Calls.DURATION)

            while (it.moveToNext()) {

                val name = it.getString(nameIndex) ?: "Unknown"
                val number = it.getString(numberIndex)
                val type = it.getInt(typeIndex)
                val date = it.getLong(dateIndex)
                val duration = it.getLong(durationIndex)


                callList.add(
                    CallLogItem(name, number, type, date, duration)
                )
            }
        }

        return callList
    }
}