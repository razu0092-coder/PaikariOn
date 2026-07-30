package com.example.ui.pages

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CartItem
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange
import com.example.viewmodel.AppNavPage

@Composable
fun CartPage(
    cartItems: List<CartItem>,
    onUpdateQuantity: (productId: String, quantity: Int) -> Unit,
    onRemoveItem: (productId: String) -> Unit,
    onOpenCodModal: () -> Unit,
    onOpenStripeModal: () -> Unit,
    onNavigate: (AppNavPage) -> Unit
) {
    val scrollState = rememberScrollState()

    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val deliveryFee = if (cartItems.isNotEmpty()) 60.0 else 0.0
    val grandTotal = subtotal + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF6F7FA))
            .padding(16.dp)
            .testTag("cart_page")
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = KomolaOrange,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "My Shopping Cart",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AshCharcoal
                        )
                    )
                    Text(
                        text = "${cartItems.sumOf { it.quantity }} items added",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Empty Cart",
                        tint = Color.Gray,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your shopping cart is currently empty",
                        fontWeight = FontWeight.Bold,
                        color = AshCharcoal,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigate(AppNavPage.ALL_PRODUCTS) },
                        colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Start Shopping Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Cart Items
            cartItems.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.product.imageUrl,
                            contentDescription = item.product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF4F5F8))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AshCharcoal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "৳${item.product.price.toInt()} x ${item.quantity}",
                                fontSize = 12.sp,
                                color = KomolaOrange,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Quantity Controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    onClick = { onUpdateQuantity(item.product.id, item.quantity - 1) },
                                    color = Color(0xFFF0F2F5),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(20.dp).padding(2.dp))
                                }

                                Text(
                                    text = item.quantity.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )

                                Surface(
                                    onClick = { onUpdateQuantity(item.product.id, item.quantity + 1) },
                                    color = Color(0xFFF0F2F5),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(20.dp).padding(2.dp))
                                }
                            }
                        }

                        IconButton(
                            onClick = { onRemoveItem(item.product.id) },
                            modifier = Modifier.testTag("remove_item_${item.product.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = Color.Red.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order Price Breakdown Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Price Summary",
                        fontWeight = FontWeight.Bold,
                        color = AshCharcoal,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Subtotal:", color = Color.Gray, fontSize = 13.sp)
                        Text(text = "৳${subtotal.toInt()}", fontWeight = FontWeight.Bold, color = AshCharcoal, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Delivery Fee (Inside Dhaka):", color = Color.Gray, fontSize = 13.sp)
                        Text(text = "৳${deliveryFee.toInt()}", fontWeight = FontWeight.Bold, color = AshCharcoal, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Payable:", fontWeight = FontWeight.Bold, color = AshCharcoal, fontSize = 15.sp)
                        Text(text = "৳${grandTotal.toInt()}", fontWeight = FontWeight.Black, color = KomolaOrange, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CHECKOUT BUTTONS
                    Button(
                        onClick = onOpenCodModal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("cart_checkout_cod_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = KomolaOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Checkout with Cash on Delivery", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onOpenStripeModal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("cart_checkout_stripe_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = AshCharcoal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = KomolaOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Pay Securely via Stripe Card", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
