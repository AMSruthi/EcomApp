package com.example.ecomapp.data.api

import com.example.ecomapp.data.model.EnquiryRequestDto
import com.example.ecomapp.data.model.ProductResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ProductResponseDto>

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductResponseDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponseDto

    @POST("carts")
    suspend fun submitEnquiry(@Body payload: EnquiryRequestDto): Any
}
