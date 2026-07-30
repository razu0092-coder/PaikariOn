package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int = 0,
    val description: String,
    val imageUrl: String,
    val rating: Float = 4.8f,
    val reviewCount: Int = 120,
    val stockQuantity: Int = 50,
    val isTopProduct: Boolean = false,
    val brand: String = "Premium Choice",
    val specDetails: String = "Warranty: 1 Year • Quality Guaranteed • Free Return"
)
