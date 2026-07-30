package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderId: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val city: String,
    val itemsSummary: String,
    val totalAmount: Double,
    val paymentMethod: String, // "Cash on Delivery" or "Stripe / Card"
    val orderStatus: String, // "Processing", "Dispatched", "Out for Delivery", "Delivered"
    val timestamp: Long = System.currentTimeMillis(),
    val trackingNumber: String,
    val isSyncedToGoogleSheet: Boolean = false,
    val notes: String = ""
)
