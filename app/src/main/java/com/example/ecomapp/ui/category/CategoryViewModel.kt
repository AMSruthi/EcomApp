package com.example.ecomapp.ui.category

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
class CategoryViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _categoryState = MutableStateFlow(CategoryUiState())
    val categoryState: StateFlow<CategoryUiState> = _categoryState.asStateFlow()

    fun selectCategory(category: String) {
        viewModelScope.launch {
            _categoryState.update { it.copy(isLoading = true, categoryName = category, errorMessage = null) }
            val result = repository.getProductsByCategory(category)
            if (result.isSuccess) {
                val catProducts = result.getOrDefault(emptyList())
                val sorted = catProducts.sortedByDescending { it.rating.rate }
                val topRated = sorted.take(2)
                val other = sorted.drop(2)

                _categoryState.update {
                    it.copy(
                        isLoading = false,
                        categoryName = category,
                        categoryTopRated = topRated,
                        categoryOther = other
                    )
                }
            } else {
                _categoryState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load products for category $category"
                    )
                }
            }
        }
    }
}
