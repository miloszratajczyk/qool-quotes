package com.example.qoolquotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val source: String,
    val author: String,
    val image: String,
    val audio: String,
)
