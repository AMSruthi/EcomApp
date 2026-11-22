package com.example.ecomapp

import com.example.ecomapp.domain.Enquiry
import com.example.ecomapp.domain.Product
import com.example.ecomapp.domain.ProductRepository
import com.example.ecomapp.domain.Rating
import com.example.ecomapp.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockProducts = listOf(
        Product(1, "Leather Jacket", 100.0, "Warm winter jacket", "clothing", "url1", Rating(4.5, 100)),
        Product(2, "Silver Ring", 50.0, "Shiny silver ring", "jewelery", "url2", Rating(4.8, 200)),
        Product(3, "Wireless Headphones", 150.0, "Noise cancelling audio", "electronics", "url3", Rating(4.2, 80))
    )

    private val fakeRepository = object : ProductRepository {
        override suspend fun getProducts() = Result.success(mockProducts)
        override suspend fun getCategories() = Result.success(listOf("clothing", "jewelery", "electronics"))
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
    fun testSearchViewModelFiltersByTitleAndDescription() = runTest {
        val viewModel = SearchViewModel(fakeRepository)
        advanceUntilIdle()

        viewModel.performSearch("Jacket")
        advanceUntilIdle()

        val state = viewModel.searchState.value
        assertEquals(1, state.searchResults.size)
        assertEquals("Leather Jacket", state.searchResults[0].title)
    }

    @Test
    fun testSearchViewModelEmptyQueryReturnsEmptyList() = runTest {
        val viewModel = SearchViewModel(fakeRepository)
        advanceUntilIdle()

        viewModel.performSearch("")
        advanceUntilIdle()

        val state = viewModel.searchState.value
        assertTrue(state.searchResults.isEmpty())
    }
}
