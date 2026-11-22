package com.example.ecomapp.ui.category

import com.example.ecomapp.domain.Product

data class CategoryUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val categoryTopRated: List<Product> = emptyList(),
    val categoryOther: List<Product> = emptyList(),
    val errorMessage: String? = null
)
