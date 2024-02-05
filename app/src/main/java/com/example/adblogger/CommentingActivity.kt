package com.example.adblogger

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adblogger.databinding.ActivityCommentingBinding
import com.example.adblogger.databinding.ActivityHomeBinding
import com.example.adblogger.model.Comment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CommentingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentingBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCommentingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val adId = intent.getStringExtra("adId").toString()

        binding.btnSubmit.setOnClickListener {
            if (binding.etComment.text!!.isEmpty()){
                Toast.makeText(this, "comment field can't be empty!", Toast.LENGTH_SHORT).show()
            }
            uploadMessages(binding.etComment.text.toString(), adId)
        }

    }


    private fun uploadMessages(message: String, id: String){
        database = FirebaseDatabase.getInstance().getReference("comments")
        auth = FirebaseAuth.getInstance()

        // Generate a unique key for the new comment
        val commentKey = database.child(id).push().key

        if (commentKey != null) {
            val commentData = Comment(message, auth.currentUser!!.email.toString())

            // Use the unique key to store the comment under the ad's reference
            database.child(id).child(commentKey)
                .setValue(commentData)
                .addOnSuccessListener { taskSnapShot ->
                    Toast.makeText(this, "Comment submitted", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Comment submission failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}