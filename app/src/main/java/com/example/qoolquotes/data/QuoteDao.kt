package com.example.qoolquotes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quote: Quote)

    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<Quote>>

}