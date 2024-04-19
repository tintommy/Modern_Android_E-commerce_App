package com.example.appbanhangonline.data

sealed class Category(val category: String) {
    object Cooker : Category("Nồi cơm")
    object Fan : Category("Quạt")
    object Refrigerator : Category("Tủ lạnh")
    object Tv : Category("Tv")
    object Washer : Category("Máy giặt")

}