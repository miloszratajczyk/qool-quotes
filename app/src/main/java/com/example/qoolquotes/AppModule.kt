package com.example.qoolquotes

import android.content.Context
import androidx.room.Room
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.data.QuoteDatabase
import com.example.qoolquotes.data.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideQuoteDao(database: QuoteDatabase): QuoteDao = database.quoteDao()

    @Provides
    fun provideRepository(dao: QuoteDao): QuoteRepository = QuoteRepository(dao)

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return QuoteDatabase.getDatabase(context);
//        return Room.databaseBuilder(
//            context.applicationContext,
//            QuoteDatabase::class.java,
//            "quote_database"
//        ).fallbackToDestructiveMigration().build()
    }
}