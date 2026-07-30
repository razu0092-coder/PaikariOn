package com.example.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Order
import com.example.model.Product
import com.example.model.UserProfile
import com.example.util.formatImageUrl
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange
import java.util.UUID

@Composable
fun AdminPage(
    isAdminLoggedIn: Boolean,
    loginError: String?,
    products: List<Product>,
    orders: List<Order>,
    userProfile: UserProfile?,
    onLoginAdmin: (String, String) -> Boolean,
    onLogoutAdmin: () -> Unit,
    onAddOrUpdateProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onDeleteOrder: (String) -> Unit,
    onUpdateOrderStatus: (String, String) -> Unit,
    onResetProductsDefault: () -> Unit,
    onSyncFromSheets: () -> Unit
) {
    if (!isAdminLoggedIn) {
        AdminLoginScreen(
            loginError = loginError,
            onLoginAdmin = onLoginAdmin
        )
    } else {
        AdminDashboardScreen(
            products = products,
            orders = orders,
            userProfile = userProfile,
            onLogoutAdmin = onLogoutAdmin,
            onAddOrUpdateProduct = onAddOrUpdateProduct,
            onDeleteProduct = onDeleteProduct,
            onDeleteOrder = onDeleteOrder,
            onUpdateOrderStatus = onUpdateOrderStatus,
            onResetProductsDefault = onResetProductsDefault,
            onSyncFromSheets = onSyncFromSheets
        )
    }
}

@Composable
private fun AdminLoginScreen(
    loginError: String?,
    onLoginAdmin: (String, String) -> Boolean
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E232A))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(KomolaOrange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Lock",
                        tint = KomolaOrange,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CEO Admin Control Center",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = AshCharcoal
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Restricted CEO Portal • Add, Edit & Delete Data",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                // Error Message Display
                if (!loginError.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = loginError,
                            color = Color(0xFFC62828),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Username Input
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_username_input"),
                    label = { Text("Username") },
                    placeholder = { Text("e.g. CEO") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.SupervisorAccount,
                            contentDescription = "User Icon",
                            tint = KomolaOrange
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KomolaOrange,
                        focusedLabelColor = KomolaOrange
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_password_input"),
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password Icon",
                            tint = KomolaOrange
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KomolaOrange,
                        focusedLabelColor = KomolaOrange
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = { onLoginAdmin(username, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("admin_login_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LOGIN TO CEO CONTROL",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Authorized Access Only\nUsername: CEO | Password required",
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminDashboardScreen(
    products: List<Product>,
    orders: List<Order>,
    userProfile: UserProfile?,
    onLogoutAdmin: () -> Unit,
    onAddOrUpdateProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onDeleteOrder: (String) -> Unit,
    onUpdateOrderStatus: (String, String) -> Unit,
    onResetProductsDefault: () -> Unit,
    onSyncFromSheets: () -> Unit
) {
    var activeTab by remember { mutableStateOf(0) } // 0: Products, 1: Orders, 2: System Sync
    var showProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }
    var orderToDelete by remember { mutableStateOf<Order?>(null) }
    var showResetCatalogConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F9))
    ) {
        // CEO Header Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = AshCharcoal),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(KomolaOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "CEO Badge",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CEO Admin Control",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Logged in as CEO (Active)",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = onLogoutAdmin,
                    modifier = Modifier.testTag("admin_logout_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Logout", fontSize = 12.sp)
                }
            }
        }

        // Section Navigation Tabs
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.White,
            contentColor = KomolaOrange
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                modifier = Modifier.testTag("admin_tab_products"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Products (${products.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                modifier = Modifier.testTag("admin_tab_orders"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Orders (${orders.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                modifier = Modifier.testTag("admin_tab_sync"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync / Reset", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (activeTab) {
                0 -> {
                    // Products Management Tab
                    AdminProductsTab(
                        products = products,
                        onAddNewClick = {
                            editingProduct = null
                            showProductDialog = true
                        },
                        onEditProduct = { prod ->
                            editingProduct = prod
                            showProductDialog = true
                        },
                        onDeleteClick = { prod ->
                            productToDelete = prod
                        }
                    )
                }
                1 -> {
                    // Orders Management Tab
                    AdminOrdersTab(
                        orders = orders,
                        onUpdateStatus = onUpdateOrderStatus,
                        onDeleteOrder = { ord -> orderToDelete = ord }
                    )
                }
                2 -> {
                    // Database & Sheets Sync Tab
                    AdminSyncTab(
                        userProfile = userProfile,
                        onSyncFromSheets = onSyncFromSheets,
                        onResetDefault = { showResetCatalogConfirm = true }
                    )
                }
            }
        }
    }

    // Modal Dialog: Add / Edit Product
    if (showProductDialog) {
        AddEditProductDialog(
            editingProduct = editingProduct,
            onDismiss = { showProductDialog = false },
            onSave = { prod ->
                onAddOrUpdateProduct(prod)
                showProductDialog = false
            }
        )
    }

    // Confirm Delete Product
    productToDelete?.let { prod ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = {
                Text(
                    text = "Delete Product?",
                    fontWeight = FontWeight.Bold,
                    color = AshCharcoal
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete '${prod.name}' (ID: ${prod.id})? This action cannot be undone.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProduct(prod.id)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete Permanently", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Confirm Delete Order
    orderToDelete?.let { ord ->
        AlertDialog(
            onDismissRequest = { orderToDelete = null },
            title = {
                Text(
                    text = "Delete Order Log?",
                    fontWeight = FontWeight.Bold,
                    color = AshCharcoal
                )
            },
            text = {
                Text(
                    text = "Remove order record ${ord.orderId} for customer ${ord.customerName}? This will delete it from local app records.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteOrder(ord.orderId)
                        orderToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete Order", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { orderToDelete = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Confirm Reset Catalog
    if (showResetCatalogConfirm) {
        AlertDialog(
            onDismissRequest = { showResetCatalogConfirm = false },
            title = {
                Text(
                    text = "Reset Product Catalog?",
                    fontWeight = FontWeight.Bold,
                    color = AshCharcoal
                )
            },
            text = {
                Text(
                    text = "This will restore the product catalog back to the official default items list. Custom added products will be overwritten.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetProductsDefault()
                        showResetCatalogConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange)
                ) {
                    Text("Reset Catalog", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetCatalogConfirm = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun AdminProductsTab(
    products: List<Product>,
    onAddNewClick: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onDeleteClick: (Product) -> Unit
) {
    var searchFilter by remember { mutableStateOf("") }

    val filtered = products.filter {
        searchFilter.isBlank() ||
                it.name.contains(searchFilter, ignoreCase = true) ||
                it.category.contains(searchFilter, ignoreCase = true) ||
                it.id.contains(searchFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = searchFilter,
                onValueChange = { searchFilter = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
                    .testTag("admin_product_search"),
                placeholder = { Text("Filter products...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = KomolaOrange)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = onAddNewClick,
                modifier = Modifier
                    .height(52.dp)
                    .testTag("admin_add_product_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Item", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No products found matching '$searchFilter'.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { product ->
                    ProductAdminCard(
                        product = product,
                        onEdit = { onEditProduct(product) },
                        onDelete = { onDeleteClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductAdminCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image Thumbnail
            AsyncImage(
                model = formatImageUrl(product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEEEEEE))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = AshCharcoal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (product.isTopProduct) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(KomolaOrange)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Top", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Text(
                    text = "ID: ${product.id} • Category: ${product.category}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "৳${product.price.toInt()}",
                        fontWeight = FontWeight.Black,
                        color = KomolaOrange,
                        fontSize = 15.sp
                    )
                    if (product.originalPrice > product.price) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "৳${product.originalPrice.toInt()}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Stock: ${product.stockQuantity}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (product.stockQuantity > 0) Color(0xFF2E7D32) else Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Actions: Edit & Delete
            Row {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("admin_edit_product_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Product",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("admin_delete_product_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Product",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminOrdersTab(
    orders: List<Order>,
    onUpdateStatus: (String, String) -> Unit,
    onDeleteOrder: (Order) -> Unit
) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No orders placed yet.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders, key = { it.orderId }) { order ->
                OrderAdminCard(
                    order = order,
                    onUpdateStatus = { newStatus -> onUpdateStatus(order.orderId, newStatus) },
                    onDelete = { onDeleteOrder(order) }
                )
            }
        }
    }
}

@Composable
private fun OrderAdminCard(
    order: Order,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    val statuses = listOf("Processing", "Shipped", "Delivered", "Cancelled")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${order.orderId} (${order.trackingNumber})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AshCharcoal
                )
                Text(
                    text = "৳${order.totalAmount.toInt()}",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = KomolaOrange
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Customer: ${order.customerName} (${order.customerPhone})",
                fontSize = 12.sp,
                color = Color.Black
            )
            Text(
                text = "Address: ${order.deliveryAddress}, ${order.city}",
                fontSize = 11.sp,
                color = Color.Gray
            )
            Text(
                text = "Items: ${order.itemsSummary}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = AshCharcoal,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Payment: ${order.paymentMethod}", fontSize = 11.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Selector Pills
            Text(text = "Change Order Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                statuses.forEach { st ->
                    val isSelected = (order.orderStatus.equals(st, ignoreCase = true))
                    val chipBg = if (isSelected) {
                        when (st) {
                            "Delivered" -> Color(0xFF2E7D32)
                            "Cancelled" -> Color(0xFFC62828)
                            "Shipped" -> Color(0xFF0288D1)
                            else -> KomolaOrange
                        }
                    } else Color(0xFFF0F2F5)

                    val chipContent = if (isSelected) Color.White else Color.DarkGray

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(chipBg)
                            .clickable { onUpdateStatus(st) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = st,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = chipContent
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Order",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminSyncTab(
    userProfile: UserProfile?,
    onSyncFromSheets: () -> Unit,
    onResetDefault: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = KomolaOrange)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Google Sheets Inventory Sync",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pull and synchronize real-time product prices, stock, and item catalog directly from connected Google Sheets.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onSyncFromSheets,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sync Now with Google Sheets", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = Color(0xFFD32F2F))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reset Product Catalog",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Restore catalog back to default seed data if needed.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = onResetDefault,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Reset Catalog to Default Items", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Active Profile Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Customer Registration Profiles Log",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (userProfile != null) {
                    Text("Latest Registered Customer:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AshCharcoal)
                    Text("Name: ${userProfile.name}", fontSize = 12.sp)
                    Text("Phone: ${userProfile.phone}", fontSize = 12.sp)
                    Text("Email: ${userProfile.email}", fontSize = 12.sp)
                    Text("Address: ${userProfile.address}, ${userProfile.city}", fontSize = 12.sp)
                } else {
                    Text("No customer registration cached locally yet.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun AddEditProductDialog(
    editingProduct: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    val isEdit = editingProduct != null

    var name by remember { mutableStateOf(editingProduct?.name ?: "") }
    var category by remember { mutableStateOf(editingProduct?.category ?: "Electronics") }
    var priceStr by remember { mutableStateOf(editingProduct?.price?.toInt()?.toString() ?: "") }
    var origPriceStr by remember { mutableStateOf(editingProduct?.originalPrice?.toInt()?.toString() ?: "") }
    var stockStr by remember { mutableStateOf(editingProduct?.stockQuantity?.toString() ?: "50") }
    var imageUrl by remember { mutableStateOf(editingProduct?.imageUrl ?: "") }
    var description by remember { mutableStateOf(editingProduct?.description ?: "") }
    var isTopProduct by remember { mutableStateOf(editingProduct?.isTopProduct ?: false) }
    var brand by remember { mutableStateOf(editingProduct?.brand ?: "PaikariOn Choice") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Electronics", "Fashion", "Gadgets", "Home & Kitchen", "Beauty & Health")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEdit) "Edit Product Item" else "Add New Product Item",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = AshCharcoal
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_dialog_name"),
                    singleLine = true
                )

                // Category selection pills
                Text("Category *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        val isSel = (category.equals(cat, ignoreCase = true))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) KomolaOrange else Color(0xFFEEEEEE))
                                .clickable { category = cat }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(cat, fontSize = 10.sp, color = if (isSel) Color.White else Color.Black)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.drop(3).forEach { cat ->
                        val isSel = (category.equals(cat, ignoreCase = true))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) KomolaOrange else Color(0xFFEEEEEE))
                                .clickable { category = cat }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(cat, fontSize = 10.sp, color = if (isSel) Color.White else Color.Black)
                        }
                    }
                }

                // Prices & Stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Price (৳) *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_dialog_price"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = origPriceStr,
                        onValueChange = { origPriceStr = it },
                        label = { Text("Regular (৳)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = stockStr,
                        onValueChange = { stockStr = it },
                        label = { Text("Stock") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                // Image URL
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_dialog_image"),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("admin_dialog_desc"),
                    maxLines = 4
                )

                // Brand
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Brand Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Top Product Switch / Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isTopProduct = !isTopProduct }
                ) {
                    Checkbox(
                        checked = isTopProduct,
                        onCheckedChange = { isTopProduct = it },
                        colors = CheckboxDefaults.colors(checkedColor = KomolaOrange)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mark as Featured / Top Product",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceVal = priceStr.toDoubleOrNull()
                    if (name.isBlank()) {
                        errorMessage = "Product name cannot be empty."
                        return@Button
                    }
                    if (priceVal == null || priceVal <= 0) {
                        errorMessage = "Please enter a valid price."
                        return@Button
                    }

                    val origPriceVal = origPriceStr.toDoubleOrNull() ?: (priceVal * 1.2)
                    val stockVal = stockStr.toIntOrNull() ?: 50
                    val formattedImg = formatImageUrl(imageUrl)
                    val finalImg = formattedImg.ifBlank { "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500" }
                    val finalId = editingProduct?.id ?: "P-${UUID.randomUUID().toString().take(6).uppercase()}"
                    val discountPct = if (origPriceVal > priceVal) (((origPriceVal - priceVal) / origPriceVal) * 100).toInt() else 0

                    val productToSave = Product(
                        id = finalId,
                        name = name.trim(),
                        category = category,
                        price = priceVal,
                        originalPrice = origPriceVal,
                        discountPercent = discountPct,
                        description = description.ifBlank { "High quality authentic product with guarantee." },
                        imageUrl = finalImg,
                        stockQuantity = stockVal,
                        isTopProduct = isTopProduct,
                        brand = brand.ifBlank { "PaikariOn Choice" }
                    )

                    onSave(productToSave)
                },
                colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange),
                modifier = Modifier.testTag("admin_dialog_save_btn")
            ) {
                Text(if (isEdit) "Update Product" else "Add Product", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}
