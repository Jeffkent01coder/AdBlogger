package com.example.adblogger.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adblogger.R
import com.example.adblogger.databinding.ActivityAboutBinding

class About : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAboutBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}