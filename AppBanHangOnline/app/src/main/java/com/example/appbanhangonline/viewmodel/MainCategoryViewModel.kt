package com.example.appbanhangonline.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore

) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProduct = _specialProducts.asStateFlow()

    private val _bestDeal = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDeal = _bestDeal.asStateFlow()

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct = _bestProduct.asStateFlow()

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProduct()
    }

    //get dữ liệu từ firebase
    fun fetchSpecialProducts() {
        if (!pagingInfo.isSpecialProductsPageEnd) {
            viewModelScope.launch {
                _specialProducts.emit(Resource.Loading())
            }
            firestore.collection("Products")
                .whereEqualTo("category", "Top sản phẩm")
                .limit(pagingInfo.specialProductsPage * 10)
                .get()
                .addOnSuccessListener { result ->
                    val specialProductsList = result.toObjects(Product::class.java)

                    pagingInfo.isSpecialProductsPageEnd =
                        specialProductsList == pagingInfo.oldSpecialProducts
                    pagingInfo.oldSpecialProducts = specialProductsList
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.Success(specialProductsList))
                    }

                    pagingInfo.specialProductsPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchBestDeals() {
        if (!pagingInfo.isBestDealsPageEnd) {
            viewModelScope.launch {
                _bestDeal.emit(Resource.Loading())
            }
            firestore.collection("Products")
               .whereEqualTo("category", "Giảm giá")
                .limit(pagingInfo.bestDealsPage*10)
                .get()
                .addOnSuccessListener { result ->
                    val bestDealsList = result.toObjects(Product::class.java)
                    pagingInfo.isBestDealsPageEnd = bestDealsList == pagingInfo.oldBestDealsProducts
                    pagingInfo.oldBestDealsProducts = bestDealsList
                    viewModelScope.launch {
                        _bestDeal.emit(Resource.Success(bestDealsList))
                    }
                    pagingInfo.bestDealsPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _bestDeal.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchBestProduct() {
        if (!pagingInfo.isBestProductsPageEnd) {
            viewModelScope.launch {
                _bestProduct.emit(Resource.Loading())
            }
            firestore.collection("Products")
                .whereEqualTo("category", "Sản phẩm tốt")
                .orderBy("id", Query.Direction.ASCENDING)
                .limit(pagingInfo.bestProductsPage * 10)
                .get()
                .addOnSuccessListener { result ->
                    val bestProductsList = result.toObjects(Product::class.java)
                    pagingInfo.isBestProductsPageEnd =
                        bestProductsList == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = bestProductsList
                    viewModelScope.launch {
                        _bestProduct.emit(Resource.Success(bestProductsList))
                    }
                    pagingInfo.bestProductsPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _bestProduct.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }


}

internal data class PagingInfo(
    var specialProductsPage: Long = 1,
    var oldSpecialProducts: List<Product> = emptyList(),
    var isSpecialProductsPageEnd: Boolean = false,

    var bestDealsPage: Long = 1,
    var oldBestDealsProducts: List<Product> = emptyList(),
    var isBestDealsPageEnd: Boolean = false,

    var bestProductsPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isBestProductsPageEnd: Boolean = false


)