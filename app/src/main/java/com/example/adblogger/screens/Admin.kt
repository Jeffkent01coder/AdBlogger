package com.example.adblogger.screens

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adblogger.databinding.ActivityAdminBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Admin : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.selectImage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.selectImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.SHOWALL.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        binding.uploadBtn.setOnClickListener {
            validateData()
        }

        binding.logout.setOnClickListener {
            auth.signOut()
        }


    }

    private var adDesc = ""
    private var adImage = ""
    private fun validateData() {
        adDesc = binding.desc.text.toString()
        adImage = binding.selectImage.toString().trim()
        if (binding.desc.text.toString().isEmpty() || imageUri == null
        ) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }

    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Advert")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val timeStamp = System.currentTimeMillis()
        val filePathAndName = "ad/$timeStamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapShot ->
                val uriask: Task<Uri> = taskSnapShot.storage.downloadUrl
                while (!uriask.isSuccessful);
                val uploadedImageUrl = "${uriask.result}"

                uploadImageInfoToDb(uploadedImageUrl, timeStamp)

                progressDialog.dismiss()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploading to Storage Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadImageInfoToDb(uploadedImageUrl: String, timeStamp: Long) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timeStamp"
        hashMap["uid"] = "$uid"
        hashMap["desc"] = "$adDesc"
        hashMap["imageUrl"] = "$uploadedImageUrl"

        val ref = FirebaseDatabase.getInstance().getReference("Adverts")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                binding.desc.text!!.clear()
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploaded",
                    Toast.LENGTH_SHORT
                ).show()
                imageUri = null

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploading to Storage Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }
}