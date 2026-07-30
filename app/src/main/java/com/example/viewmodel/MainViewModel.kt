package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ECommerceRepository
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.HeaderTheme
import com.example.model.Order
import com.example.model.Product
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavPage {
    HOME,
    ALL_PRODUCTS,
    ABOUT_US,
    OUR_POLICY,
    ORDER_TRACKING,
    CART,
    ADMIN_CONTROL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = ECommerceRepository(application)

    // Admin Auth State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    // Current page in the main site sections
    private val _currentPage = MutableStateFlow(AppNavPage.HOME)
    val currentPage: StateFlow<AppNavPage> = _currentPage.asStateFlow()

    // Header Theme Choice (Ash BG vs Komola Orange BG)
    val headerTheme: StateFlow<HeaderTheme> = repository.headerTheme

    // Products from Repository
    val allProducts: StateFlow<List<Product>> = repository.allProductsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected product for popup detail dialog
    val selectedProduct: StateFlow<Product?> = repository.selectedProduct

    // Active Category Filter
    val selectedCategory: StateFlow<String> = repository.selectedCategory

    // Search Query
    val searchQuery: StateFlow<String> = repository.searchQuery

    // Cart Items
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems

    // User Profile
    val userProfile: StateFlow<UserProfile?> = repository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // All Placed Orders
    val allOrders: StateFlow<List<Order>> = repository.allOrdersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Modals visibility state
    private val _showCodOrderModal = MutableStateFlow(false)
    val showCodOrderModal: StateFlow<Boolean> = _showCodOrderModal.asStateFlow()

    private val _showStripeModal = MutableStateFlow(false)
    val showStripeModal: StateFlow<Boolean> = _showStripeModal.asStateFlow()

    private val _showProfileModal = MutableStateFlow(false)
    val showProfileModal: StateFlow<Boolean> = _showProfileModal.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    private val _showOrderSuccessDialog = MutableStateFlow(false)
    val showOrderSuccessDialog: StateFlow<Boolean> = _showOrderSuccessDialog.asStateFlow()

    // Filtered Products based on search query and selected category
    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        selectedCategory,
        searchQuery
    ) { products, category, query ->
        products.filter { product ->
            val matchesCategory = (category == "All" || product.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Top Featured Products
    val topProducts: StateFlow<List<Product>> = allProducts
        .combine(MutableStateFlow(Unit)) { products, _ ->
            products.filter { it.isTopProduct || it.discountPercent >= 20 }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Static Categories List
    val categories: List<Category> = listOf(
        Category("C1", "Electronics", "ic_electronics", 42),
        Category("C2", "Fashion", "ic_fashion", 68),
        Category("C3", "Gadgets", "ic_gadgets", 35),
        Category("C4", "Home & Kitchen", "ic_home", 50),
        Category("C5", "Beauty & Health", "ic_beauty", 29)
    )

    init {
        // Fetch inventory on start
        viewModelScope.launch {
            repository.refreshInventoryFromSheets()
        }
    }

    fun navigateTo(page: AppNavPage) {
        _currentPage.value = page
    }

    fun setHeaderTheme(theme: HeaderTheme) {
        repository.setHeaderTheme(theme)
    }

    fun toggleHeaderTheme() {
        repository.toggleHeaderTheme()
    }

    fun setSearchQuery(query: String) {
        repository.setSearchQuery(query)
    }

    fun setSelectedCategory(category: String) {
        repository.setSelectedCategory(category)
    }

    fun openProductPopup(product: Product) {
        repository.openProductDetail(product)
    }

    fun closeProductPopup() {
        repository.closeProductDetail()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        repository.addToCart(product, quantity)
    }

    fun removeFromCart(productId: String) {
        repository.removeFromCart(productId)
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        repository.updateCartQuantity(productId, quantity)
    }

    fun openCodOrderModal() {
        _showCodOrderModal.value = true
    }

    fun closeCodOrderModal() {
        _showCodOrderModal.value = false
    }

    fun openStripeModal() {
        _showStripeModal.value = true
    }

    fun closeStripeModal() {
        _showStripeModal.value = false
    }

    fun openProfileModal() {
        _showProfileModal.value = true
    }

    fun closeProfileModal() {
        _showProfileModal.value = false
    }

    fun closeOrderSuccessDialog() {
        _showOrderSuccessDialog.value = false
    }

    fun launchWhatsApp(product: Product? = null) {
        repository.launchWhatsAppContact(product)
    }

    fun placeCodOrder(
        name: String,
        phone: String,
        address: String,
        city: String,
        singleProduct: Product? = null,
        quantity: Int = 1,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val itemsToOrder = if (singleProduct != null) {
                listOf(CartItem(product = singleProduct, quantity = quantity))
            } else {
                cartItems.value
            }

            if (itemsToOrder.isEmpty()) return@launch

            val order = repository.placeOrder(
                customerName = name,
                customerPhone = phone,
                address = address,
                city = city,
                paymentMethod = "Cash on Delivery",
                items = itemsToOrder,
                notes = notes
            )

            // Save user profile to Sheet 1
            repository.saveUserProfile(
                UserProfile(
                    name = name,
                    email = "$phone@customer.com",
                    phone = phone,
                    address = address,
                    city = city
                )
            )

            _lastPlacedOrder.value = order
            _showCodOrderModal.value = false
            repository.closeProductDetail()
            _showOrderSuccessDialog.value = true
        }
    }

    fun placeStripeOrder(
        name: String,
        phone: String,
        address: String,
        city: String,
        cardNumber: String,
        singleProduct: Product? = null,
        quantity: Int = 1
    ) {
        viewModelScope.launch {
            val itemsToOrder = if (singleProduct != null) {
                listOf(CartItem(product = singleProduct, quantity = quantity))
            } else {
                cartItems.value
            }

            if (itemsToOrder.isEmpty()) return@launch

            val last4Digits = cardNumber.takeLast(4).ifEmpty { "4242" }

            val order = repository.placeOrder(
                customerName = name,
                customerPhone = phone,
                address = address,
                city = city,
                paymentMethod = "Stripe Card (**** $last4Digits)",
                items = itemsToOrder,
                notes = "Paid via Stripe Card"
            )

            repository.saveUserProfile(
                UserProfile(
                    name = name,
                    email = "$phone@customer.com",
                    phone = phone,
                    address = address,
                    city = city
                )
            )

            _lastPlacedOrder.value = order
            _showStripeModal.value = false
            repository.closeProductDetail()
            _showOrderSuccessDialog.value = true
        }
    }

    fun saveRegistrationProfile(name: String, email: String, phone: String, address: String, city: String) {
        viewModelScope.launch {
            repository.saveUserProfile(
                UserProfile(
                    name = name,
                    email = email,
                    phone = phone,
                    address = address,
                    city = city
                )
            )
            _showProfileModal.value = false
        }
    }

    // --- ADMIN CONTROL FUNCTIONS ---
    fun loginAdmin(username: String, pass: String): Boolean {
        if (username.trim() == "CEO" && pass == "razu441155go") {
            _isAdminLoggedIn.value = true
            _adminLoginError.value = null
            return true
        } else {
            _adminLoginError.value = "Invalid Username or Password! Access restricted to CEO."
            return false
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _adminLoginError.value = null
    }

    fun addOrUpdateProduct(product: Product) {
        viewModelScope.launch {
            repository.addOrUpdateProduct(product)
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }

    fun resetProductsToDefault() {
        viewModelScope.launch {
            repository.resetProductsToDefaultCatalog()
        }
    }

    fun syncInventoryFromSheets() {
        viewModelScope.launch {
            repository.refreshInventoryFromSheets()
        }
    }
}
