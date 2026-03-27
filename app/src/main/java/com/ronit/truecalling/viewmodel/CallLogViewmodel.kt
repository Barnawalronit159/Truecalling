package com.ronit.truecalling.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronit.truecalling.model.CallLogItem
import com.ronit.truecalling.repository.CallLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CallLogViewModel : ViewModel() {

    private val repository = CallLogRepository()

    private val _logs = MutableStateFlow<List<CallLogItem>>(emptyList())
    val logs: StateFlow<List<CallLogItem>> = _logs

    fun loadLogs(context: Context) {
        viewModelScope.launch {
            _logs.value = repository.getCallLogs(context)
        }
    }
}