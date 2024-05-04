package com.example.appbanhangonline.helper

fun Float?.getProductPrice(price:Float):Float{
    if(this== null)
        return price
    val remainingPricePercentage= 1f-this.div(100)
    val priceAfterOffer = remainingPricePercentage* price
    return priceAfterOffer
}