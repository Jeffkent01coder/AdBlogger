package com.example.adblogger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.adblogger.screens.Home
import com.example.adblogger.screens.Login
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if (auth.currentUser != null) {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}