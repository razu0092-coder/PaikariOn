package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.HeaderTheme
import com.example.model.Product
import com.example.ui.components.CodOrderFormModal
import com.example.ui.components.HeaderSection
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.ProductDetailPopup
import com.example.ui.components.RegistrationProfileModal
import com.example.ui.components.StripeCheckoutModal
import com.example.ui.pages.AboutUsPage
import com.example.ui.pages.AdminPage
import com.example.ui.pages.AllProductsPage
import com.example.ui.pages.CartPage
import com.example.ui.pages.HomePage
import com.example.ui.pages.OrderTrackingPage
import com.example.ui.pages.OurPolicyPage
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppNavPage
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                ECommerceAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ECommerceAppContent(viewModel: MainViewModel) {
    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()
    val headerTheme by viewModel.headerTheme.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()

    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val topProducts by viewModel.topProducts.collectAsStateWithLifecycle()
    val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()

    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsStateWithLifecycle()
    val adminLoginError by viewModel.adminLoginError.collectAsStateWithLifecycle()

    val showCodModal by viewModel.showCodOrderModal.collectAsStateWithLifecycle()
    val showStripeModal by viewModel.showStripeModal.collectAsStateWithLifecycle()
    val showProfileModal by viewModel.showProfileModal.collectAsStateWithLifecycle()
    val showSuccessDialog by viewModel.showOrderSuccessDialog.collectAsStateWithLifecycle()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeaderSection(
                headerTheme = headerTheme,
                currentPage = currentPage,
                searchQuery = searchQuery,
                cartItemCount = cartItems.sumOf { it.quantity },
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onToggleTheme = { viewModel.toggleHeaderTheme() },
                onOpenProfile = { viewModel.openProfileModal() },
                onOpenCart = { viewModel.navigateTo(AppNavPage.CART) },
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF6F7FA))
        ) {
            when (currentPage) {
                AppNavPage.HOME -> {
                    HomePage(
                        topProducts = topProducts,
                        categories = viewModel.categories,
                        onProductClick = { viewModel.openProductPopup(it) },
                        onCategoryClick = { viewModel.setSelectedCategory(it) },
                        onNavigate = { viewModel.navigateTo(it) },
                        onWhatsAppCall = { viewModel.launchWhatsApp() }
                    )
                }
                AppNavPage.ALL_PRODUCTS -> {
                    AllProductsPage(
                        products = filteredProducts,
                        categories = viewModel.categories,
                        selectedCategory = selectedCategory,
                        headerTheme = headerTheme,
                        onCategorySelect = { viewModel.setSelectedCategory(it) },
                        onProductClick = { viewModel.openProductPopup(it) },
                        onSelectHeaderTheme = { viewModel.setHeaderTheme(it) }
                    )
                }
                AppNavPage.ABOUT_US -> {
                    AboutUsPage(
                        onWhatsAppCall = { viewModel.launchWhatsApp() }
                    )
                }
                AppNavPage.OUR_POLICY -> {
                    OurPolicyPage()
                }
                AppNavPage.ORDER_TRACKING -> {
                    OrderTrackingPage(
                        orders = allOrders
                    )
                }
                AppNavPage.CART -> {
                    CartPage(
                        cartItems = cartItems,
                        onUpdateQuantity = { id, q -> viewModel.updateCartQuantity(id, q) },
                        onRemoveItem = { id -> viewModel.removeFromCart(id) },
                        onOpenCodModal = { viewModel.openCodOrderModal() },
                        onOpenStripeModal = { viewModel.openStripeModal() },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppNavPage.ADMIN_CONTROL -> {
                    AdminPage(
                        isAdminLoggedIn = isAdminLoggedIn,
                        loginError = adminLoginError,
                        products = allProducts,
                        orders = allOrders,
                        userProfile = userProfile,
                        onLoginAdmin = { u, p -> viewModel.loginAdmin(u, p) },
                        onLogoutAdmin = { viewModel.logoutAdmin() },
                        onAddOrUpdateProduct = { prod -> viewModel.addOrUpdateProduct(prod) },
                        onDeleteProduct = { id -> viewModel.deleteProduct(id) },
                        onDeleteOrder = { id -> viewModel.deleteOrder(id) },
                        onUpdateOrderStatus = { id, st -> viewModel.updateOrderStatus(id, st) },
                        onResetProductsDefault = { viewModel.resetProductsToDefault() },
                        onSyncFromSheets = { viewModel.syncInventoryFromSheets() }
                    )
                }
            }

            // POPUP MODAL 1: Product Details Popup on SAME page
            selectedProduct?.let { product ->
                ProductDetailPopup(
                    product = product,
                    onDismiss = { viewModel.closeProductPopup() },
                    onWhatsAppCall = { viewModel.launchWhatsApp(it) },
                    onOrderNowCod = { prod, q ->
                        viewModel.addToCart(prod, q)
                        viewModel.openCodOrderModal()
                    },
                    onPayStripe = { prod, q ->
                        viewModel.addToCart(prod, q)
                        viewModel.openStripeModal()
                    },
                    onAddToCart = { prod, q ->
                        viewModel.addToCart(prod, q)
                        viewModel.closeProductPopup()
                    }
                )
            }

            // POPUP MODAL 2: Cash on Delivery Form
            if (showCodModal) {
                CodOrderFormModal(
                    singleProduct = selectedProduct,
                    cartItems = cartItems,
                    userProfile = userProfile,
                    onDismiss = { viewModel.closeCodOrderModal() },
                    onSubmitOrder = { name, phone, address, city, notes ->
                        viewModel.placeCodOrder(name, phone, address, city, selectedProduct, 1, notes)
                    }
                )
            }

            // POPUP MODAL 3: Stripe Checkout Form
            if (showStripeModal) {
                StripeCheckoutModal(
                    singleProduct = selectedProduct,
                    cartItems = cartItems,
                    userProfile = userProfile,
                    onDismiss = { viewModel.closeStripeModal() },
                    onSubmitStripePayment = { name, phone, address, city, cardNum ->
                        viewModel.placeStripeOrder(name, phone, address, city, cardNum, selectedProduct, 1)
                    }
                )
            }

            // POPUP MODAL 4: User Registration & Profile (Sheet 1)
            if (showProfileModal) {
                RegistrationProfileModal(
                    userProfile = userProfile,
                    onDismiss = { viewModel.closeProfileModal() },
                    onSaveProfile = { name, email, phone, address, city ->
                        viewModel.saveRegistrationProfile(name, email, phone, address, city)
                    }
                )
            }

            // POPUP MODAL 5: Order Success Confirmation Dialog
            if (showSuccessDialog && lastPlacedOrder != null) {
                OrderSuccessDialog(
                    order = lastPlacedOrder!!,
                    onDismiss = { viewModel.closeOrderSuccessDialog() },
                    onTrackOrder = {
                        viewModel.closeOrderSuccessDialog()
                        viewModel.navigateTo(AppNavPage.ORDER_TRACKING)
                    }
                )
            }
        }
    }
}
