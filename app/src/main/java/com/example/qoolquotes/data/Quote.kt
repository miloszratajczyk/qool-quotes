package com.example.qoolquotes.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SourceType(val label: String) {
    BOOK("książka"), MOVIE("film"), SONG("piosenka"), OTHER("inne");

    fun toLabel(): String = label
}

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "photo") val photoUri: Uri = Uri.EMPTY,
    @ColumnInfo(name = "audio") val audioUri: Uri = Uri.EMPTY,
    @ColumnInfo(name = "sourcetype") val sourceType: SourceType
)