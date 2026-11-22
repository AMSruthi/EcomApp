package com.example.ecomapp

import com.example.ecomapp.domain.Enquiry
import com.example.ecomapp.domain.Product
import com.example.ecomapp.domain.ProductRepository
import com.example.ecomapp.domain.Rating
import com.example.ecomapp.ui.category.CategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockProducts = listOf(
        Product(1, "Electronics A", 10.0, "Desc A", "electronics", "url1", Rating(4.5, 100)),
        Product(2, "Electronics B", 20.0, "Desc B", "electronics", "url2", Rating(4.8, 200)),
        Product(3, "Jewelery C", 30.0, "Desc C", "jewelery", "url3", Rating(3.0, 10))
    )

    private val fakeRepository = object : ProductRepository {
        override suspend fun getProducts() = Result.success(mockProducts)
        override suspend fun getCategories() = Result.success(listOf("electronics", "jewelery"))
        override suspend fun getProductsByCategory(category: String) =
            Result.success(mockProducts.filter { it.category == category })
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
    fun testSelectCategoryLoadsFilteredProductsSuccessfully() = runTest {
        val viewModel = CategoryViewModel(fakeRepository)
        viewModel.selectCategory("electronics")
        advanceUntilIdle()

        val state = viewModel.categoryState.value
        assertEquals("electronics", state.categoryName)
        assertFalse(state.isLoading)
        assertEquals(2, state.categoryTopRated.size + state.categoryOther.size)
    }

    @Test
    fun testCategoryViewModelHandlesErrorOnFailure() = runTest {
        val failingRepo = object : ProductRepository {
            override suspend fun getProducts() = Result.failure<List<Product>>(Exception())
            override suspend fun getCategories() = Result.failure<List<String>>(Exception())
            override suspend fun getProductsByCategory(category: String) = Result.failure<List<Product>>(Exception("Category load error"))
            override suspend fun getProductById(id: Int) = Result.failure<Product>(Exception())
            override suspend fun submitEnquiry(enquiry: Enquiry) = Result.failure<Boolean>(Exception())
        }

        val viewModel = CategoryViewModel(failingRepo)
        viewModel.selectCategory("electronics")
        advanceUntilIdle()

        val state = viewModel.categoryState.value
        assertFalse(state.isLoading)
        assertNotNull(state.errorMessage)
    }
}
