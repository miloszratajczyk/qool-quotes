package com.example.qoolquotes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)

    @Update
    suspend fun updateQuote(quote: Quote)

    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<Quote>>

    @Query("SELECT COUNT(*) FROM quotes")
    fun getAllQuoteCount():  Flow<Int>

    @Query("SELECT * FROM quotes WHERE photo IS NOT NULL AND photo != ''")
    fun getQuotesWithImages(): Flow<List<Quote>>

    @Query("SELECT COUNT(*) FROM quotes WHERE photo IS NOT NULL AND photo != ''")
    fun getQuotesWithImagesCount():  Flow<Int>

    @Query("SELECT * FROM quotes WHERE audio IS NOT NULL AND audio != ''")
    fun getQuotesWithAudio(): Flow<List<Quote>>

    @Query("SELECT COUNT(*) FROM quotes WHERE audio IS NOT NULL AND audio != ''")
    fun getQuotesWithAudioCount():  Flow<Int>

    @Query("""
        SELECT * FROM quotes 
        WHERE text LIKE '%' || :query || '%' 
        OR author LIKE '%' || :query || '%' 
        OR source LIKE '%' || :query || '%' 
    """)
    fun searchQuotes(query: String): Flow<List<Quote>>

    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuote(): Flow<Quote>

    @Query("SELECT * FROM quotes WHERE id = :quoteId")
    fun getQuoteById(quoteId: Int): Flow<List<Quote>>
}
