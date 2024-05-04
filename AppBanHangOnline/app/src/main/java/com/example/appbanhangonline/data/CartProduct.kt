package com.example.appbanhangonline.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedSize: String?,
    val selectedColor: Int? = null
):Parcelable{
    constructor():this(Product(),1,null,null)
}
