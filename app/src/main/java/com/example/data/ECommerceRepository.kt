package com.example.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.model.CartItem
import com.example.model.HeaderTheme
import com.example.model.Order
import com.example.model.Product
import com.example.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.UUID

class ECommerceRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val productDao = db.productDao()
    private val userProfileDao = db.userProfileDao()
    private val orderDao = db.orderDao()

    private val sheetsService = GoogleSheetsSyncService()

    // Header Theme preference: ASH_BG vs KOMOLA_BG
    private val _headerTheme = MutableStateFlow(HeaderTheme.KOMOLA_BG)
    val headerTheme: StateFlow<HeaderTheme> = _headerTheme.asStateFlow()

    // Active Cart state
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Active Selected Product for Popup Detail view
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Active Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active Selected Category
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val allProductsFlow: Flow<List<Product>> = productDao.getAllProducts()
    val userProfileFlow: Flow<UserProfile?> = userProfileDao.getUserProfile()
    val allOrdersFlow: Flow<List<Order>> = orderDao.getAllOrders()

    fun setHeaderTheme(theme: HeaderTheme) {
        _headerTheme.value = theme
    }

    fun toggleHeaderTheme() {
        _headerTheme.value = if (_headerTheme.value == HeaderTheme.KOMOLA_BG) HeaderTheme.ASH_BG else HeaderTheme.KOMOLA_BG
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun openProductDetail(product: Product) {
        _selectedProduct.value = product
    }

    fun closeProductDetail() {
        _selectedProduct.value = null
    }

    suspend fun refreshInventoryFromSheets() = withContext(Dispatchers.IO) {
        try {
            val fetchedProducts = sheetsService.fetchInventoryFromSheet()
            if (fetchedProducts.isNotEmpty()) {
                productDao.deleteAllProducts()
                productDao.insertProducts(fetchedProducts)
            }
        } catch (e: Exception) {
            // If offline, ensure database contains default catalog
            val defaultList = sheetsService.getDefaultCatalog()
            productDao.insertProducts(defaultList)
        }
    }

    suspend fun addOrUpdateProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(productId: String) = withContext(Dispatchers.IO) {
        productDao.deleteProductById(productId)
    }

    suspend fun deleteOrder(orderId: String) = withContext(Dispatchers.IO) {
        orderDao.deleteOrderById(orderId)
    }

    suspend fun updateOrderStatus(orderId: String, status: String) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun resetProductsToDefaultCatalog() = withContext(Dispatchers.IO) {
        val defaultList = sheetsService.getDefaultCatalog()
        productDao.deleteAllProducts()
        productDao.insertProducts(defaultList)
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        _cartItems.update { current ->
            val existing = current.find { it.product.id == product.id }
            if (existing != null) {
                current.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity)
                    else it
                }
            } else {
                current + CartItem(product = product, quantity = quantity)
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.update { current -> current.filter { it.product.id != productId } }
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.update { current ->
            current.map {
                if (it.product.id == productId) it.copy(quantity = quantity)
                else it
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        userProfileDao.saveUserProfile(profile)
        sheetsService.syncRegistrationToGoogleSheet(profile)
    }

    suspend fun placeOrder(
        customerName: String,
        customerPhone: String,
        address: String,
        city: String,
        paymentMethod: String,
        items: List<CartItem>,
        notes: String = ""
    ): Order = withContext(Dispatchers.IO) {
        val total = items.sumOf { it.product.price * it.quantity }
        val summary = items.joinToString(", ") { "${it.product.name} (x${it.quantity})" }
        val randomNum = (1000..9999).random()
        val orderId = "ORD-$randomNum"
        val trackingNum = "TRK-${System.currentTimeMillis().toString().takeLast(6)}$randomNum"

        val order = Order(
            orderId = orderId,
            customerName = customerName,
            customerPhone = customerPhone,
            deliveryAddress = address,
            city = city,
            itemsSummary = summary,
            totalAmount = total,
            paymentMethod = paymentMethod,
            orderStatus = "Processing",
            timestamp = System.currentTimeMillis(),
            trackingNumber = trackingNum,
            isSyncedToGoogleSheet = true,
            notes = notes
        )

        orderDao.insertOrder(order)
        sheetsService.syncOrderToGoogleSheet(order)

        // Clear cart after order placed
        _cartItems.value = emptyList()
        order
    }

    /**
     * Requirement: Launch WhatsApp call/chat for "Know More" to number 01754441155
     */
    fun launchWhatsAppContact(product: Product? = null) {
        val phone = "8801754441155"
        val message = if (product != null) {
            "Hello! I want to know more about product: ${product.name} (ID: ${product.id}, Price: ৳${product.price})."
        } else {
            "Hello! I am contacting you from the PaikariOn app for inquiries."
        }
        val encodedMsg = Uri.encode(message)
        val url = "https://api.whatsapp.com/send?phone=$phone&text=$encodedMsg"

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to phone dialer if WhatsApp app is not installed
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01754441155")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(callIntent)
        }
    }
}
