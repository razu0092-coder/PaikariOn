package com.example.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: String = "Standard",
    val selectedOption: String = "Default"
)
