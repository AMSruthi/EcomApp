package com.example.ecomapp.data.model

data class EnquiryRequestDto(
    val productId: Int,
    val name: String,
    val email: String,
    val message: String
)
