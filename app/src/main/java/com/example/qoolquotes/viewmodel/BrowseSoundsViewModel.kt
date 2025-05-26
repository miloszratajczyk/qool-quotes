package com.example.qoolquotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class BrowseSoundsViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    val audioQuotes: StateFlow<List<Quote>> = repository.getAllQuotes()
        .map { quotes ->
            quotes.filter { it.audioUri != android.net.Uri.EMPTY }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}
