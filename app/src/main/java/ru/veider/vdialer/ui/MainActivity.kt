package ru.veider.vdialer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.veider.vdialer.R
import ru.veider.vdialer.databinding.MainActivityBinding
import ru.veider.vdialer.ui.contacts.ContactsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MainActivityBinding.inflate(layoutInflater).root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container,
                ContactsFragment.newInstance())
            .commitNow()
    }
}