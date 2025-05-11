package com.example.qoolquotes.data

import androidx.room.TypeConverter
import android.net.Uri

class UriTypeConverter {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return value?.let { Uri.parse(it) }
    }

    @TypeConverter
    fun uriToString(uri: Uri?): String? {
        return uri?.toString()
    }


    @TypeConverter
    fun fromStatus(value: SourceType): String = value.name

    @TypeConverter
    fun toStatus(value: String): SourceType = SourceType.valueOf(value)
}