package ru.veider.vdialer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import ru.veider.vdialer.repository.Contacts

class ContactsViewModel(
    var contactsLiveData: MutableLiveData<List<Contacts.Contact>> = MutableLiveData()
) : ViewModel() {

    fun getContacts() = contactsLiveData.postValue(Contacts().getContacts())
}