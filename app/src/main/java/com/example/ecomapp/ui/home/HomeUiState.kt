package com.example.ecomapp.ui.home

import com.example.ecomapp.domain.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val topRatedProducts: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val errorMessage: String? = null
)
