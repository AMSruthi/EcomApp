package com.example.ecomapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.domain.Enquiry
import com.example.ecomapp.domain.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _detailState = MutableStateFlow(ProductDetailUiState())
    val detailState: StateFlow<ProductDetailUiState> = _detailState.asStateFlow()

    fun loadProductDetail(productId: Int) {
        viewModelScope.launch {
            _detailState.update { it.copy(isLoading = true, enquirySubmitted = false, errorMessage = null) }
            val result = repository.getProductById(productId)
            if (result.isSuccess) {
                _detailState.update {
                    it.copy(
                        isLoading = false,
                        product = result.getOrNull()
                    )
                }
            } else {
                _detailState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Product not found"
                    )
                }
            }
        }
    }

    fun submitEnquiry(name: String, email: String, message: String) {
        val product = _detailState.value.product ?: return
        viewModelScope.launch {
            _detailState.update { it.copy(isEnquiring = true, errorMessage = null) }
            val enquiry = Enquiry(
                productId = product.id,
                productTitle = product.title,
                name = name,
                email = email,
                message = message
            )
            val result = repository.submitEnquiry(enquiry)
            if (result.isSuccess) {
                _detailState.update {
                    it.copy(
                        isEnquiring = false,
                        enquirySubmitted = true
                    )
                }
            } else {
                _detailState.update {
                    it.copy(
                        isEnquiring = false,
                        errorMessage = "Failed to submit enquiry. Please try again."
                    )
                }
            }
        }
    }

    fun resetEnquiryStatus() {
        _detailState.update { it.copy(enquirySubmitted = false) }
    }
}
