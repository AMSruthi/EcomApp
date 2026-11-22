package com.example.ecomapp

import com.example.ecomapp.domain.Enquiry
import com.example.ecomapp.domain.Product
import com.example.ecomapp.domain.ProductRepository
import com.example.ecomapp.domain.Rating
import com.example.ecomapp.ui.detail.ProductDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockProducts = listOf(
        Product(1, "Product A", 10.0, "Desc A", "electronics", "url1", Rating(4.5, 100))
    )

    private val fakeRepository = object : ProductRepository {
        override suspend fun getProducts() = Result.success(mockProducts)
        override suspend fun getCategories() = Result.success(emptyList<String>())
        override suspend fun getProductsByCategory(category: String) = Result.success(emptyList<Product>())
        override suspend fun getProductById(id: Int) = Result.success(mockProducts.first { it.id == id })
        override suspend fun submitEnquiry(enquiry: Enquiry) = Result.success(true)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadProductDetailSuccess() = runTest {
        val viewModel = ProductDetailViewModel(fakeRepository)
        viewModel.loadProductDetail(1)
        advanceUntilIdle()

        val state = viewModel.detailState.value
        assertFalse(state.isLoading)
        assertNotNull(state.product)
        assertEquals("Product A", state.product?.title)
    }

    @Test
    fun testSubmitEnquirySuccess() = runTest {
        val viewModel = ProductDetailViewModel(fakeRepository)
        viewModel.loadProductDetail(1)
        advanceUntilIdle()

        viewModel.submitEnquiry("John Doe", "john@example.com", "Is this item available?")
        advanceUntilIdle()

        val state = viewModel.detailState.value
        assertFalse(state.isEnquiring)
        assertTrue(state.enquirySubmitted)
    }
}
