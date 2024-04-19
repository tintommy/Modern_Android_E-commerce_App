package com.example.appbanhangonline.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhangonline.data.Category
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val fireStore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    private val categoryPagingInfo = CategoryPagingInfo()


    init {
        fetchOfferProduct()
        fetchBestProducts()
    }

    fun fetchOfferProduct() {
        if (!categoryPagingInfo.isOfferProductsPageEnd) {
            viewModelScope.launch {
                _offerProducts.emit(Resource.Loading())
            }
            fireStore.collection("Products")
                .whereEqualTo("category", category.category)
                .whereNotEqualTo("offerPercentage", null)

                .get()
                .addOnSuccessListener {
                    val productsList = it.toObjects(Product::class.java)
                    categoryPagingInfo.isOfferProductsPageEnd =
                        productsList == categoryPagingInfo.oldOfferProducts
                    categoryPagingInfo.oldOfferProducts = productsList
                    viewModelScope.launch {
                        _offerProducts.emit(Resource.Success(productsList))
                        Log.e("Cooker",productsList.toString())
                    }
                    categoryPagingInfo.offerProductsPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _offerProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchBestProducts() {
        if (!categoryPagingInfo.isBestProductsPageEnd) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }
            fireStore.collection("Products")
                .whereEqualTo("category", category.category)
                .whereEqualTo("offerPercentage", null)
                .limit(categoryPagingInfo.bestProductsPage * 10)
                .get()
                .addOnSuccessListener {
                    val bestProductList = it.toObjects(Product::class.java)
                    categoryPagingInfo.isBestProductsPageEnd =
                        bestProductList == categoryPagingInfo.oldBestProducts
                    categoryPagingInfo.oldBestProducts = bestProductList
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(bestProductList))
                        Log.e("Cooker",bestProductList.toString())
                    }
                    categoryPagingInfo.bestProductsPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }

    }
}

internal data class CategoryPagingInfo(
    var offerProductsPage: Long = 1,
    var oldOfferProducts: List<Product> = emptyList(),
    var isOfferProductsPageEnd: Boolean = false,

    var bestProductsPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isBestProductsPageEnd: Boolean = false


)