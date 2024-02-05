package com.example.adblogger.adapter

import CommentsAdapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adblogger.CommentingActivity
import com.example.adblogger.databinding.EachItemBinding
import com.example.adblogger.model.AdData
import com.example.adblogger.model.Comment
import com.google.firebase.database.*

class ImageAdapter(private val mList: List<AdData>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ImagesViewHolder>() {

    inner class ImagesViewHolder(val binding: EachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var adData: AdData
        val likesRef: DatabaseReference
            get() = FirebaseDatabase.getInstance().getReference("Adverts")
                .child(adData.id.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding =
            EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.adData = mList[position]

        with(holder.binding) {
            with(holder.adData) {
                // Load image using Picasso or any other method
                adText.text = this.desc

                comment.setOnClickListener {
                    val intent = Intent(context, CommentingActivity::class.java)
                    intent.putExtra("adId", this.id.toString())
                    context.startActivity(intent)
                }

                val nodeRef = holder.likesRef.child("likes")

                nodeRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val likes = snapshot.getValue(Int::class.java)
                        likesCount.text = likes.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error if needed
                    }
                })

                like.setOnClickListener {
                    nodeRef.runTransaction(object : Transaction.Handler {
                        override fun doTransaction(currentData: MutableData): Transaction.Result {
                            val currentLikes = currentData.getValue(Int::class.java) ?: 0
                            currentData.value =
                                if (!currentLikesByUser) currentLikes + 1 else currentLikes - 1

                            currentLikesByUser = !currentLikesByUser

                            return Transaction.success(currentData)
                        }

                        override fun onComplete(
                            error: DatabaseError?,
                            committed: Boolean,
                            currentData: DataSnapshot?
                        ) {
                            Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                viewComments.setOnClickListener {
                    if (commentsRecyclerView.visibility == View.GONE) {
                        commentsRecyclerView.visibility = View.VISIBLE
                        title.visibility = View.VISIBLE
                        loadComments(commentsRecyclerView, this.id.toString())
                    } else {
                        commentsRecyclerView.visibility = View.GONE
                        title.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun loadComments(recyclerView: RecyclerView, adId: String) {
        val commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(adId)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsList = mutableListOf<Comment>()
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.child("comment").getValue(String::class.java)
                    val senderEmail = commentSnapshot.child("senderEmail").getValue(String::class.java)

                    if (comment != null && senderEmail != null) {
                        val commentData = Comment(comment, senderEmail)
                        commentsList.add(commentData)
                    }
                }

                // Create an adapter for the RecyclerView and set it
                val commentsAdapter = CommentsAdapter(commentsList)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = commentsAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
