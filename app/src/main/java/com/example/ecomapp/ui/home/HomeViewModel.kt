package com.example.ecomapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.domain.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true, errorMessage = null) }
            val productsResult = repository.getProducts()
            val categoriesResult = repository.getCategories()

            if (productsResult.isSuccess && categoriesResult.isSuccess) {
                val allProducts = productsResult.getOrDefault(emptyList())
                val cats = categoriesResult.getOrDefault(emptyList())
                val top5 = allProducts.sortedByDescending { it.rating.rate * it.rating.count }.take(5)

                _homeState.update {
                    it.copy(
                        isLoading = false,
                        topRatedProducts = top5,
                        categories = cats
                    )
                }
            } else {
                _homeState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load catalogue data."
                    )
                }
            }
        }
    }
}
