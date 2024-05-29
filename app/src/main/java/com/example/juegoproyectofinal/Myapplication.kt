package com.example.juegoproyectofinal

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Establecer el nivel de log de Firebase
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)
    }
}
