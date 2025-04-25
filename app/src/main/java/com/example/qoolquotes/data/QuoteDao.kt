package com.example.qoolquotes.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY author ASC")
    fun getAllQuotes(): Flow<List<Quote>>
}