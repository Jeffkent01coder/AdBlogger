package com.example.adblogger.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adblogger.databinding.ActivityAdminLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminLogin : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, AdminRegister::class.java))
        }

        binding.btnLogin.setOnClickListener {
            registerEvents()
        }
    }

    private fun registerEvents() {
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            if (userId != null) {
                                val usersRef = FirebaseDatabase.getInstance().getReference("Admins")
                                usersRef.child(userId).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val role =
                                            snapshot.child("role").getValue(String::class.java)
                                        if (role == "admin") {
                                            startActivity(
                                                Intent(
                                                    this@AdminLogin,
                                                    Admin::class.java
                                                )
                                            )
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@AdminLogin,
                                                "Access Denied",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle error
                                    }
                                })
                            }
                            binding.etEmail.text?.clear()
                            binding.passEt.text?.clear()
                            Toast.makeText(
                                this,
                                "Logged In successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        if (email.isNotEmpty() && pass.isNotEmpty()) {
                            startActivity(Intent(this, Admin::class.java))
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
                    "Empty Fields Not Allowed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
