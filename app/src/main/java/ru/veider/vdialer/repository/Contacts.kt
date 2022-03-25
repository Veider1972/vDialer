package ru.veider.vdialer.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import ru.veider.vdialer.App

class Contacts {
    private var value = ArrayList<Contact>()

    inner class Contact(var image: Bitmap?, var name: String, var phone: String)

    @SuppressLint("Range")
    fun getContacts(): List<Contact> {
        val contentResolver: ContentResolver? =
            App.getInstance()?.applicationContext?.contentResolver
        contentResolver?.let {
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val name = cursor.getString(nameColumnIndex)
                        val phoneColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val phone = cursor.getString(phoneColumnIndex)
                        val contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                        val uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
                        val stream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,uri)
                        val bitmap = BitmapFactory.decodeStream(stream)
                        value.add(Contact(bitmap, name, phone))
                    }
                }
            }
            cursorWithContacts?.close()
        }
        return value
    }
}