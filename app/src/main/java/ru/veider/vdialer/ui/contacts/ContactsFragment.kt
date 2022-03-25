package ru.veider.vdialer.ui.contacts

import PERMISSIONS
import REQUEST_CODE
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.veider.vdialer.R
import ru.veider.vdialer.databinding.ContactsFragmentBinding
import ru.veider.vdialer.repository.Contacts
import ru.veider.vdialer.viewmodel.ContactsViewModel

class ContactsFragment : Fragment() {

    private lateinit var binder: ContactsFragmentBinding
    private var contacts: List<Contacts.Contact> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binder = ContactsFragmentBinding.inflate(inflater)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        }
    }

    private fun getContacts(it: List<Contacts.Contact>?) {
        it?.let {
            contacts = it
            with (binder){
                contactsLayout.visibility = View.VISIBLE
                loadingLayout.loader.visibility = View.GONE
                val adapter = ContactsAdapter(this@ContactsFragment,contacts)
                contactsView.layoutManager = LinearLayoutManager(requireContext())
                contactsView.adapter = adapter
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initialize()
                } else {
                    AlertDialog.Builder(this.requireContext())
                        .setTitle(getString(R.string.request_title))
                        .setMessage(getString(R.string.request_message))
                        .setNegativeButton(getString(R.string.close)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                return
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED &&  ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                initialize()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS + Manifest.permission.CALL_PHONE) -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.access_title))
                    .setMessage(getString(R.string.access_explanation))
                    .setPositiveButton(getString(R.string.access_yes)) { _, _ ->
                        requestPermission()
                    }
                    .setNegativeButton(getString(R.string.access_no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            else -> {
                requestPermission()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        requestPermissions(PERMISSIONS, REQUEST_CODE)
    }

    private fun initialize(){
        ViewModelProvider(this)[ContactsViewModel::class.java].apply {
            contactsLiveData.observe(viewLifecycleOwner) { getContacts(it) }
            getContacts()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }
}