package com.example.appbanhangonline.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.firebase.FirebaseCommon
import com.example.appbanhangonline.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()


    fun addUpdateProduct(cartProduct: CartProduct) {

        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
            firestore.collection("user").document(auth.uid!!).collection("cart")
                .whereEqualTo("product.id", cartProduct.product.id)
                .get()
                .addOnSuccessListener {
                    var check = false
                    it.documents.let {
                        if (it.isEmpty()) {//them san pham moi
                            addNewProduct(cartProduct)
                            Log.e("MyTag", "empty")
                        } else {
                            var productTemp: CartProduct? = null
                            for (i in 0 until it.size) {
                                productTemp = it.get(i).toObject(CartProduct::class.java)
                                if (productTemp!!.selectedColor == cartProduct.selectedColor
                                    && productTemp.selectedSize.equals(cartProduct.selectedSize)
                                    && productTemp.product.id == cartProduct.product.id
                                ) {
                                    val documentId = it.get(i).id
                                    increaseQuantity(documentId, cartProduct)
                                    check = true
                                    Log.e("MyTag", "exist1")
                                    Log.e("MyTag", "exist1" + " " + productTemp.product.id)
                                    Log.e("MyTag", "exist1" + " " + cartProduct.product.id)
                                }
                            }
                            if (!check) {//them moi
                                addNewProduct(cartProduct)
                                Log.e("MyTag", "exist2")
                                Log.e("MyTag", "exist2" + " " + productTemp!!.product.id)
                                Log.e("MyTag", "exist2" + " " + cartProduct.product.id)
                            }

                        }
                    }
                    // _addToCart.emit(Resource.Success(it.));
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _addToCart.emit(Resource.Error(it.message.toString()))
                    }

                }
        }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId, cartProduct.quantity) { addedId, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))


            }
        }
    }
}