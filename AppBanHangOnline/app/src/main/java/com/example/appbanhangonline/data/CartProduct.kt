package com.example.appbanhangonline.data

data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedSize: String?,
    val selectedColor: Int? = null
){
    constructor():this(Product(),1,null,null)
}
