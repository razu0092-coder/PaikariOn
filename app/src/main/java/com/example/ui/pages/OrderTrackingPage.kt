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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange
import com.example.ui.theme.SuccessGreen

@Composable
fun OrderTrackingPage(
    orders: List<Order>
) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val filteredOrders = if (searchQuery.isBlank()) {
        orders
    } else {
        orders.filter {
            it.orderId.contains(searchQuery, ignoreCase = true) ||
            it.trackingNumber.contains(searchQuery, ignoreCase = true) ||
            it.customerPhone.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF6F7FA))
            .padding(16.dp)
            .testTag("order_tracking_page")
    ) {
        // Page Title Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = "Track Order",
                        tint = KomolaOrange,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Live Order Delivery Tracking",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = AshCharcoal
                            )
                        )
                        Text(
                            text = "Synced with Google Sheet #3 (Order Delivery Data)",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Order ID (ORD-1024), Tracking #, or Phone...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tracking_search_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredOrders.isEmpty()) {
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
                        imageVector = Icons.Outlined.Inventory2,
                        contentDescription = "No Orders",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isBlank()) "No orders found in Sheet 3 database." else "No matching orders found for '$searchQuery'.",
                        fontWeight = FontWeight.Bold,
                        color = AshCharcoal,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Place a Cash on Delivery or Stripe order to view live delivery status.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            filteredOrders.forEach { order ->
                OrderTrackingCard(order = order)
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun OrderTrackingCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("order_card_${order.orderId}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order ID: ${order.orderId}",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = AshCharcoal
                    )
                    Text(
                        text = "Tracking #: ${order.trackingNumber}",
                        fontSize = 11.sp,
                        color = KomolaOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = SuccessGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = order.orderStatus,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))

            // Items & Delivery Info
            Text(text = "Customer: ${order.customerName} (${order.customerPhone})", fontSize = 12.sp, color = AshCharcoal)
            Text(text = "Address: ${order.deliveryAddress}, ${order.city}", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Items: ${order.itemsSummary}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = AshCharcoal)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Payment: ${order.paymentMethod}", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "Total: ৳${order.totalAmount.toInt()}",
                    fontWeight = FontWeight.Black,
                    color = KomolaOrange,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Status Stepper
            Text(text = "Delivery Tracking Progress (Sheet 3):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AshCharcoal)
            Spacer(modifier = Modifier.height(8.dp))

            val stages = listOf("Placed", "Processing", "Dispatched", "Delivered")
            val currentStageIndex = when (order.orderStatus) {
                "Processing" -> 1
                "Dispatched" -> 2
                "Out for Delivery" -> 2
                "Delivered" -> 3
                else -> 1
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                stages.forEachIndexed { index, stage ->
                    val isPassed = index <= currentStageIndex
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isPassed) KomolaOrange else Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isPassed) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stage,
                            fontSize = 10.sp,
                            fontWeight = if (isPassed) FontWeight.Bold else FontWeight.Normal,
                            color = if (isPassed) AshCharcoal else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
