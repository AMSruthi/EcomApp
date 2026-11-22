package com.example.ecomapp.domain

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getCategories(): Result<List<String>>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun submitEnquiry(enquiry: Enquiry): Result<Boolean>
}
