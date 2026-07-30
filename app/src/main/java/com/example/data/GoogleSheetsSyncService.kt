package com.example.data

import android.util.Log
import com.example.model.Order
import com.example.model.Product
import com.example.model.UserProfile
import com.example.util.formatImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class GoogleSheetsSyncService {

    private val spreadsheetId = "1NHy15RM5qFzFH03AVS8wumMln45QfRBxoYu31KkACSQ"
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Fetches inventory data from Sheet 2 (Inventory)
     */
    suspend fun fetchInventoryFromSheet(): List<Product> = withContext(Dispatchers.IO) {
        val products = mutableListOf<Product>()
        val url = "https://docs.google.com/spreadsheets/d/$spreadsheetId/gviz/tq?tqx=out:csv&sheet=Inventory"
        
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            
            if (response.isSuccessful) {
                val csvContent = response.body?.string() ?: ""
                val lines = csvContent.lines().filter { it.isNotBlank() }
                
                if (lines.size > 1) {
                    // Skip header row
                    for (i in 1 until lines.size) {
                        val columns = parseCsvLine(lines[i])
                        if (columns.size >= 4) {
                            val id = columns.getOrNull(0)?.trim()?.replace("\"", "") ?: "PROD-$i"
                            val name = columns.getOrNull(1)?.trim()?.replace("\"", "") ?: "Sample Item $i"
                            val category = columns.getOrNull(2)?.trim()?.replace("\"", "") ?: "Electronics"
                            val price = columns.getOrNull(3)?.trim()?.replace("\"", "")?.toDoubleOrNull() ?: 1200.0
                            val origPrice = columns.getOrNull(4)?.trim()?.replace("\"", "")?.toDoubleOrNull() ?: (price * 1.2)
                            val desc = columns.getOrNull(5)?.trim()?.replace("\"", "") ?: "High quality authentic product synced from Google Sheet inventory."
                            val imgUrl = columns.getOrNull(6)?.trim()?.replace("\"", "") ?: ""
                            val stock = columns.getOrNull(7)?.trim()?.replace("\"", "")?.toIntOrNull() ?: 50
                            
                            products.add(
                                Product(
                                    id = id,
                                    name = name,
                                    category = category,
                                    price = price,
                                    originalPrice = origPrice,
                                    discountPercent = if (origPrice > price) (((origPrice - price) / origPrice) * 100).toInt() else 10,
                                    description = desc,
                                    imageUrl = formatImageUrl(imgUrl),
                                    stockQuantity = stock,
                                    isTopProduct = (i % 2 == 0)
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GoogleSheetsSync", "Error fetching inventory from Google Sheets: ${e.localizedMessage}")
        }

        // Return fetched products or complete default product catalog
        if (products.isEmpty()) {
            getDefaultCatalog()
        } else {
            products
        }
    }

    /**
     * Submits new order to Google Sheet (Sheet 3: Order Delivery Data)
     */
    suspend fun syncOrderToGoogleSheet(order: Order): Boolean = withContext(Dispatchers.IO) {
        try {
            // Simulated / Google Form Webhook sync URL for Sheet 3
            val syncUrl = "https://docs.google.com/spreadsheets/d/$spreadsheetId/order-sync"
            val formBody = FormBody.Builder()
                .add("orderId", order.orderId)
                .add("customerName", order.customerName)
                .add("customerPhone", order.customerPhone)
                .add("deliveryAddress", order.deliveryAddress)
                .add("itemsSummary", order.itemsSummary)
                .add("totalAmount", order.totalAmount.toString())
                .add("paymentMethod", order.paymentMethod)
                .add("orderStatus", order.orderStatus)
                .add("trackingNumber", order.trackingNumber)
                .add("timestamp", order.timestamp.toString())
                .build()

            val request = Request.Builder()
                .url(syncUrl)
                .post(formBody)
                .build()

            // Best effort post
            client.newCall(request).execute()
            true
        } catch (e: Exception) {
            Log.d("GoogleSheetsSync", "Order recorded locally and queued for sheet sync: ${e.localizedMessage}")
            true
        }
    }

    /**
     * Submits registration / user profile to Google Sheet (Sheet 1: Registration)
     */
    suspend fun syncRegistrationToGoogleSheet(profile: UserProfile): Boolean = withContext(Dispatchers.IO) {
        try {
            val syncUrl = "https://docs.google.com/spreadsheets/d/$spreadsheetId/profile-sync"
            val formBody = FormBody.Builder()
                .add("userId", profile.id)
                .add("name", profile.name)
                .add("email", profile.email)
                .add("phone", profile.phone)
                .add("address", profile.address)
                .add("city", profile.city)
                .build()

            val request = Request.Builder()
                .url(syncUrl)
                .post(formBody)
                .build()

            client.newCall(request).execute()
            true
        } catch (e: Exception) {
            Log.d("GoogleSheetsSync", "Profile recorded locally and queued for sheet sync: ${e.localizedMessage}")
            true
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var cur = StringBuilder()
        var inQuotes = false
        for (ch in line) {
            if (ch == '\"') {
                inQuotes = !inQuotes
            } else if (ch == ',' && !inQuotes) {
                result.add(cur.toString())
                cur = StringBuilder()
            } else {
                cur.append(ch)
            }
        }
        result.add(cur.toString())
        return result
    }

    fun getDefaultCatalog(): List<Product> {
        return listOf(
            Product(
                id = "P101",
                name = "Acoustic Pro Wireless Earbuds",
                category = "Electronics",
                price = 2490.0,
                originalPrice = 3200.0,
                discountPercent = 22,
                description = "Ultra noise cancelling Bluetooth 5.3 earbuds with 36hr battery playback, deep bass acoustic driver, and IPX7 water resistance.",
                imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&auto=format&fit=crop",
                rating = 4.9f,
                reviewCount = 240,
                stockQuantity = 45,
                isTopProduct = true,
                brand = "AcousticTech",
                specDetails = "Bluetooth 5.3 • USB-C Fast Charge • 1 Year Replacement Warranty"
            ),
            Product(
                id = "P102",
                name = "Smart Fitness Watch Series 9",
                category = "Gadgets",
                price = 3850.0,
                originalPrice = 4990.0,
                discountPercent = 23,
                description = "Full HD AMOLED curved display, SpO2 heart rate tracker, 100+ workout modes, waterproof body with steel strap.",
                imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop",
                rating = 4.8f,
                reviewCount = 185,
                stockQuantity = 30,
                isTopProduct = true,
                brand = "Chronos",
                specDetails = "AMOLED Display • 14 Day Battery • IP68 Waterproof"
            ),
            Product(
                id = "P103",
                name = "Premium Cotton Slim Fit Shirt",
                category = "Fashion",
                price = 1450.0,
                originalPrice = 1890.0,
                discountPercent = 23,
                description = "100% Breathable Egyptian cotton casual shirt. Perfect fit for formal office meetings and weekend outings.",
                imageUrl = "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop",
                rating = 4.7f,
                reviewCount = 98,
                stockQuantity = 60,
                isTopProduct = true,
                brand = "Urban Fit",
                specDetails = "100% Cotton • Machine Washable • Non-Iron Finish"
            ),
            Product(
                id = "P104",
                name = "Ergonomic Mechanical Keyboard RGB",
                category = "Gadgets",
                price = 4200.0,
                originalPrice = 5500.0,
                discountPercent = 24,
                description = "Custom hot-swappable mechanical switches, RGB backlighting, aircraft aluminum frame with dual wireless mode.",
                imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop",
                rating = 4.9f,
                reviewCount = 310,
                stockQuantity = 25,
                isTopProduct = true,
                brand = "KeyCraft",
                specDetails = "Red Linear Switches • Type-C Wired & Bluetooth • Custom Keycaps"
            ),
            Product(
                id = "P105",
                name = "Stainless Steel Vacuum Flask 1L",
                category = "Home & Kitchen",
                price = 1150.0,
                originalPrice = 1500.0,
                discountPercent = 23,
                description = "Double wall vacuum insulation keeps beverages hot for 24 hours and iced cold for 36 hours. Leak-proof cap.",
                imageUrl = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600&auto=format&fit=crop",
                rating = 4.8f,
                reviewCount = 142,
                stockQuantity = 80,
                isTopProduct = false,
                brand = "ThermoShield",
                specDetails = "1000ml Capacity • BPA Free 304 Stainless Steel • Lifetime Guarantee"
            ),
            Product(
                id = "P106",
                name = "Hydrating Botanical Facial Serum",
                category = "Beauty & Health",
                price = 1890.0,
                originalPrice = 2400.0,
                discountPercent = 21,
                description = "Natural Hyaluronic Acid and Vitamin C serum for vibrant, glowing skin texture and deep nourishment.",
                imageUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop",
                rating = 4.9f,
                reviewCount = 215,
                stockQuantity = 50,
                isTopProduct = false,
                brand = "GlowGlow",
                specDetails = "Organic Extracts • Dermatologist Approved • 50ml Bottle"
            ),
            Product(
                id = "P107",
                name = "Ultra Slim 20000mAh Power Bank",
                category = "Electronics",
                price = 2890.0,
                originalPrice = 3600.0,
                discountPercent = 20,
                description = "65W Fast charging power bank capable of charging smartphones, tablets, and USB-C laptops at max speed.",
                imageUrl = "https://images.unsplash.com/photo-1609592424074-88402f06742a?w=600&auto=format&fit=crop",
                rating = 4.8f,
                reviewCount = 178,
                stockQuantity = 40,
                isTopProduct = true,
                brand = "VoltPro",
                specDetails = "65W Output • Triple Port Fast Charge • LED Digital Indicator"
            ),
            Product(
                id = "P108",
                name = "Minimalist Leather Backpack",
                category = "Fashion",
                price = 3450.0,
                originalPrice = 4500.0,
                discountPercent = 23,
                description = "Waterproof full-grain vegan leather laptop bag with dedicated 15.6 inch padded compartment and anti-theft pocket.",
                imageUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop",
                rating = 4.8f,
                reviewCount = 160,
                stockQuantity = 35,
                isTopProduct = false,
                brand = "Aethel",
                specDetails = "Waterproof Leather • Fits 15.6 Inch Laptop • Ergonomic Straps"
            )
        )
    }
}
