package com.example.adblogger.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adblogger.R
import com.example.adblogger.databinding.ActivityRegisterBinding
import com.example.adblogger.model.UserDetails
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textViewSignIn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        auth = FirebaseAuth.getInstance()
        registerEvents()
    }


    private var email = ""
    private fun registerEvents() {
        binding.nextBtn.setOnClickListener {
            email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val verifyPass = binding.verifyPassEt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()) {
                if (pass == verifyPass) {
                    binding.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful) {
                                createUserDetails(imageUrl)
                                Toast.makeText(
                                    this,
                                    "Registration successful",
                                    Toast.LENGTH_SHORT
                                ).show()


                            } else {
                                Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            if (email.isNotEmpty() && pass.isNotEmpty()) {
                                startActivity(Intent(this, Home::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            binding.progressBar.visibility = View.GONE
                        })
                } else {
                    Toast.makeText(
                        this,
                        "Passwords Don't Match",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    this,
                    "Empty Fields Not Allowed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun createUserDetails(imageUrl: String?) {
        val imageData = UserDetails(imageUrl)
        database = Firebase.database.reference
        database.child("users").child(auth.currentUser!!.uid).setValue(imageData)
            .addOnCompleteListener {
                Toast.makeText(this, "uploaded!!", Toast.LENGTH_SHORT).show()
            }
    }


}