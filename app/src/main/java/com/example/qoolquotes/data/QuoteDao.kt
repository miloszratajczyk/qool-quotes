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
    suspend fun getAllQuotes(): Flow<List<Quote>>

    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun getAllQuoteCount(): Int


    @Query("SELECT * FROM quotes WHERE photo IS NOT NULL AND photo != ''")
    suspend fun getQuotesWithImages(): Flow<List<Quote>>

    @Query("SELECT COUNT(*) FROM quotes WHERE photo IS NOT NULL AND photo != ''")
    suspend fun getQuotesWithImagesCount(): Int


    @Query("SELECT * FROM quotes WHERE audio IS NOT NULL AND audio != ''")
    suspend fun getQuotesWithAudio(): Flow<List<Quote>>

    @Query("SELECT COUNT(*) FROM quotes WHERE photo IS NOT NULL AND photo != ''")
    suspend fun getQuotesWithAudioCount(): Int

    @Query("""
        SELECT * FROM quotes 
        WHERE text LIKE '%' || :query || '%' 
        OR author LIKE '%' || :query || '%' 
        OR source LIKE '%' || :query || '%' 
        OR photo IS NOT NULL AND photo != '' 
        OR audio IS NOT NULL AND audio != ''
    """)
    suspend fun searchQuotes(query: String): Flow<List<Quote>>

}