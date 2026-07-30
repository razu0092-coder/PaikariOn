package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "user_default",
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val postalCode: String = "1200",
    val isSyncedToSheet: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
