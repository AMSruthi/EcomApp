package com.example.ecomapp

import com.example.ecomapp.domain.Enquiry
import com.example.ecomapp.domain.Product
import com.example.ecomapp.domain.ProductRepository
import com.example.ecomapp.domain.Rating
import com.example.ecomapp.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockProducts = listOf(
        Product(1, "Product A", 10.0, "Desc A", "electronics", "url1", Rating(4.5, 100)),
        Product(2, "Product B", 20.0, "Desc B", "jewelery", "url2", Rating(4.8, 200)),
        Product(3, "Product C", 30.0, "Desc C", "electronics", "url3", Rating(3.0, 10))
    )

    private val fakeRepository = object : ProductRepository {
        override suspend fun getProducts() = Result.success(mockProducts)
        override suspend fun getCategories() = Result.success(listOf("electronics", "jewelery"))
        override suspend fun getProductsByCategory(category: String) = Result.success(emptyList<Product>())
        override suspend fun getProductById(id: Int) = Result.success(mockProducts.first())
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
    fun testHomeViewModelLoadsProductsAndCategoriesSuccessfully() = runTest {
        val viewModel = HomeViewModel(fakeRepository)
        advanceUntilIdle()

        val state = viewModel.homeState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(2, state.categories.size)
        assertEquals(3, state.topRatedProducts.size)
    }

    @Test
    fun testHomeViewModelHandlesRepositoryFailure() = runTest {
        val failingRepo = object : ProductRepository {
            override suspend fun getProducts() = Result.failure<List<Product>>(Exception("Network error"))
            override suspend fun getCategories() = Result.failure<List<String>>(Exception("Network error"))
            override suspend fun getProductsByCategory(category: String) = Result.failure<List<Product>>(Exception())
            override suspend fun getProductById(id: Int) = Result.failure<Product>(Exception())
            override suspend fun submitEnquiry(enquiry: Enquiry) = Result.failure<Boolean>(Exception())
        }

        val viewModel = HomeViewModel(failingRepo)
        advanceUntilIdle()

        val state = viewModel.homeState.value
        assertFalse(state.isLoading)
        assertEquals("Failed to load catalogue data.", state.errorMessage)
    }
}
