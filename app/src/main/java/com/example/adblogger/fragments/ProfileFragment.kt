package com.example.adblogger.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adblogger.databinding.FragmentProfileBinding
import com.example.adblogger.screens.AdminLogin
import com.example.adblogger.screens.Login
import com.example.adblogger.screens.Profile
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.logout.setOnClickListener {
            logOut()
        }


        binding.loginAdmin.setOnClickListener {
            val intent = Intent(requireActivity(), AdminLogin::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(requireActivity(), Profile::class.java)
            startActivity(intent)
        }

    }

    private fun logOut() {
        auth.signOut()
        startActivity(Intent(requireActivity(), Login::class.java))
        activity?.finish()
    }
}