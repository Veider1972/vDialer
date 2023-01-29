package ru.veider.vdialer

import android.app.Application

class App:Application() {

    companion object {
        private var application: Application? = null
        fun getInstance() = application
    }

    override fun onCreate() {
        application = this
        super.onCreate()
    }
}