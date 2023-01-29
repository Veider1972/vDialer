package ru.veider.vdialer.ui.contacts

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ru.veider.vdialer.R
import ru.veider.vdialer.databinding.ContactItemBinding
import ru.veider.vdialer.repository.Contacts

class ContactsAdapter(
    val contactsFragment: ContactsFragment,
    private val contacts: List<Contacts.Contact>
) :
    RecyclerView.Adapter<ContactsAdapter.ContactsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        val binder = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsHolder(binder.root)
    }

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    inner class ContactsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var contact: Contacts.Contact

        init {
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_CALL,Uri.parse("tel:" + contact.phone))
                startActivity(contactsFragment.requireContext(), intent, null)
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(contact: Contacts.Contact) {
            this.contact = contact
            with(ContactItemBinding.bind(itemView)) {
                contact.image?.let { contactImage.setImageBitmap(contact.image) } ?: contactImage.setImageDrawable(itemView.context.getDrawable(R.drawable.contact))
                contactName.text = contact.name
                contactPhone.text = contact.phone
            }
        }
    }

}