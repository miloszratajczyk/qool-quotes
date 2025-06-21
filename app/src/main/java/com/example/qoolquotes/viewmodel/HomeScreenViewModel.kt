package com.example.qoolquotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    val quoteCount: StateFlow<Int> = repository.getAllQuoteCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val quotesWithImagesCount: StateFlow<Int> = repository.getQuotesWithImagesCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val quotesWithAudioCount: StateFlow<Int> = repository.getQuotesWithAudioCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val randomQuote: StateFlow<Quote?> = repository.getRandomQuote()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}
