package com.example.adblogger.model

data class AdData(
    val id: String? = "",
    val imageUrl : String? = "",
    val desc : String? = "",
    val likes: Int = 0,
    var currentLikesByUser: Boolean = false,
    var likedBy: MutableMap<String, Boolean> = mutableMapOf()
)
