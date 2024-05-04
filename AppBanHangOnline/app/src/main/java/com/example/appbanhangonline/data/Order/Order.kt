package com.example.appbanhangonline.data.Order

import com.example.appbanhangonline.data.Address
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.data.Product

data class Order(
    val orderStatus: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address:Address

)
