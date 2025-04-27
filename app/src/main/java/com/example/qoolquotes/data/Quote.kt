package com.example.qoolquotes.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "photo") val photoUri: Uri?,
    @ColumnInfo(name = "audio") val audioUri: Uri?
)