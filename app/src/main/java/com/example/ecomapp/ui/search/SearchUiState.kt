package com.example.ecomapp.ui.search

import com.example.ecomapp.domain.Product

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val errorMessage: String? = null
)
