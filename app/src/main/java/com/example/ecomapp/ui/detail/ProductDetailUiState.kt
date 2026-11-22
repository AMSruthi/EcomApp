package com.example.ecomapp.ui.detail

import com.example.ecomapp.domain.Product

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val isEnquiring: Boolean = false,
    val enquirySubmitted: Boolean = false,
    val errorMessage: String? = null
)
