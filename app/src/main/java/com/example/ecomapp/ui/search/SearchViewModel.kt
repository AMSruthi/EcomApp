package com.example.ecomapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.domain.Product
import com.example.ecomapp.domain.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    private var allProductsCache: List<Product> = emptyList()

    init {
        viewModelScope.launch {
            repository.getProducts().onSuccess {
                allProductsCache = it
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchState.update { it.copy(searchQuery = query) }
    }

    fun performSearch(query: String) {
        viewModelScope.launch {
            _searchState.update { it.copy(isLoading = true, searchQuery = query, errorMessage = null) }
            if (allProductsCache.isEmpty()) {
                repository.getProducts().onSuccess { allProductsCache = it }
            }
            val results = if (query.isBlank()) {
                emptyList()
            } else {
                allProductsCache.filter { 
                    it.title.contains(query, ignoreCase = true) || 
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
                }
            }
            _searchState.update {
                it.copy(
                    isLoading = false,
                    searchResults = results
                )
            }
        }
    }
}
