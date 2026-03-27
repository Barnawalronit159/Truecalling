package com.ronit.truecalling.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronit.truecalling.model.Contact
import com.ronit.truecalling.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel : ViewModel() {

    private val repository = ContactRepository()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun loadContacts(context: Context) {
        viewModelScope.launch {
            _contacts.value = repository.getContacts(context)
        }
    }
}