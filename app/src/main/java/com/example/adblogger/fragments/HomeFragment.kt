package com.example.adblogger.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adblogger.adapter.ImageAdapter
import com.example.adblogger.databinding.FragmentHomeBinding
import com.example.adblogger.model.AdData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private var mList = mutableListOf<AdData>()
    private lateinit var adapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVars()
        getImages()
        Log.d("testValue", "this is a test")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImages() {
        database = Firebase.database.reference
        database.child("Adverts").get()
            .addOnSuccessListener {dataSnapshot ->
                for (advertSnapshot in dataSnapshot.children) {
                    val id = advertSnapshot.child("id").getValue(String::class.java)
                    val description = advertSnapshot.child("desc").getValue(String::class.java)
                    val imageUrl = advertSnapshot.child("imageUrl").getValue(String::class.java)

                    if (imageUrl != null && description != null) {
                        val adData = AdData(id, imageUrl, description)
                        mList.add(adData)
                        Log.d("AdvertData", "Description: $description, Image URL: $imageUrl")
                    }
                }
            adapter.notifyDataSetChanged()

        }
    }






    private fun initVars() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = ImageAdapter(mList, requireContext())
        binding.recyclerView.adapter = adapter

    }

}