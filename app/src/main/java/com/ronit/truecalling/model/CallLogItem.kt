package com.ronit.truecalling.model

data class CallLogItem(
    val name: String?,
    val number: String,
    val type: Int,
    val date: Long,
    val duration: Long
)
