package com.example.qoolquotes.data


import kotlinx.coroutines.flow.Flow

class QuoteRepository(private val quoteDao: QuoteDao) {

    // Fetch all quotes
     fun getAllQuotes(): Flow<List<Quote>> = quoteDao.getAllQuotes()

    // Fetch count of all quotes
     fun getAllQuoteCount(): Flow<Int> = quoteDao.getAllQuoteCount()

    // Fetch quotes with images
     fun getQuotesWithImages(): Flow<List<Quote>> = quoteDao.getQuotesWithImages()

    // Fetch count of quotes with images
     fun getQuotesWithImagesCount(): Flow<Int> = quoteDao.getQuotesWithImagesCount()

    // Fetch quotes with audio
     fun getQuotesWithAudio(): Flow<List<Quote>> = quoteDao.getQuotesWithAudio()

    // Fetch count of quotes with audio
     fun getQuotesWithAudioCount(): Flow<Int> = quoteDao.getQuotesWithAudioCount()

    // Search quotes based on text, author, source, or media content
     fun searchQuotes(query: String): Flow<List<Quote>> = quoteDao.searchQuotes(query)

    // Fetch a random quote
     fun getRandomQuote(): Flow<Quote> = quoteDao.getRandomQuote()


    // Insert a quote
    suspend fun insertQuote(quote: Quote) {
        quoteDao.insertQuote(quote)
    }

    // Delete a quote
    suspend fun deleteQuote(quote: Quote) {
        quoteDao.delete(quote)
    }

    // Update a quote
    suspend fun updateQuote(quote: Quote) {
        quoteDao.updateQuote(quote)
    }
}