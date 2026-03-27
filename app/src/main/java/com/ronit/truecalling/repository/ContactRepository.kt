package com.ronit.truecalling.repository

import android.content.Context
import android.provider.ContactsContract
import com.ronit.truecalling.model.Contact

class ContactRepository {

    fun getContacts(context: Context): List<Contact> {

        val contactList = mutableListOf<Contact>()
        val seenNumbers = mutableSetOf<String>()   // To remove duplicasy

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {

            val nameIndex = it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val numberIndex = it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            while (it.moveToNext()) {

                val name = it.getString(nameIndex)
                val rawNumber = it.getString(numberIndex)

               // normalize number
                val cleanNumber = rawNumber
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("+91", "")

                //  remove duplicates
                if (!seenNumbers.contains(cleanNumber)) {
                    seenNumbers.add(cleanNumber)
                    contactList.add(Contact(name, rawNumber))
                }
            }
        }

        return contactList
    }
}