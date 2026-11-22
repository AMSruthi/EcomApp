package com.example.ecomapp.data.model

data class ProductResponseDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingResponseDto
)
