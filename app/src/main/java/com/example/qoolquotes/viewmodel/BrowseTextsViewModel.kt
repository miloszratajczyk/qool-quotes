package com.example.qoolquotes.viewmodel

import android.net.Uri
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
import kotlinx.coroutines.launch

@HiltViewModel
class BrowseTextsViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    val allQuotes: StateFlow<List<Quote>> = repository.getAllQuotes()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun deleteQuote(quote: Quote) {
        viewModelScope.launch {
            repository.deleteQuote(quote)
        }
    }
}
