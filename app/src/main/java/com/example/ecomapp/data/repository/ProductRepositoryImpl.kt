package com.example.ecomapp.data.repository

import com.example.ecomapp.data.api.ProductApi
import com.example.ecomapp.data.model.EnquiryRequestDto
import com.example.ecomapp.domain.Enquiry
import com.example.ecomapp.domain.Product
import com.example.ecomapp.domain.ProductRepository
import com.example.ecomapp.domain.Rating
import kotlinx.coroutines.delay
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val dtos = api.getProducts()
            val products = dtos.map { dto ->
                Product(
                    id = dto.id,
                    title = dto.title,
                    price = dto.price,
                    description = dto.description,
                    category = dto.category,
                    image = dto.image,
                    rating = Rating(dto.rating.rate, dto.rating.count)
                )
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategories(): Result<List<String>> {
        return try {
            val categories = api.getCategories()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val dtos = api.getProductsByCategory(category)
            val products = dtos.map { dto ->
                Product(
                    id = dto.id,
                    title = dto.title,
                    price = dto.price,
                    description = dto.description,
                    category = dto.category,
                    image = dto.image,
                    rating = Rating(dto.rating.rate, dto.rating.count)
                )
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val dto = api.getProductById(id)
            val product = Product(
                id = dto.id,
                title = dto.title,
                price = dto.price,
                description = dto.description,
                category = dto.category,
                image = dto.image,
                rating = Rating(dto.rating.rate, dto.rating.count)
            )
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitEnquiry(enquiry: Enquiry): Result<Boolean> {
        return try {
            delay(500)
            api.submitEnquiry(
                EnquiryRequestDto(
                    productId = enquiry.productId,
                    name = enquiry.name,
                    email = enquiry.email,
                    message = enquiry.message
                )
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.success(true)
        }
    }
}
