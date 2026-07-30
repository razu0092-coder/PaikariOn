package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CartItem
import com.example.model.Product
import com.example.model.UserProfile
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange

@Composable
fun CodOrderFormModal(
    singleProduct: Product? = null,
    quantity: Int = 1,
    cartItems: List<CartItem> = emptyList(),
    userProfile: UserProfile? = null,
    onDismiss: () -> Unit,
    onSubmitOrder: (name: String, phone: String, address: String, city: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf(userProfile?.name ?: "") }
    var phone by remember { mutableStateOf(userProfile?.phone ?: "") }
    var address by remember { mutableStateOf(userProfile?.address ?: "") }
    var city by remember { mutableStateOf(userProfile?.city?.ifEmpty { "Dhaka" } ?: "Dhaka") }
    var notes by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    val totalAmount = if (singleProduct != null) {
        singleProduct.price * quantity
    } else {
        cartItems.sumOf { it.product.price * it.quantity }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .testTag("cod_order_form_modal"),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Modal Title & Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "COD Order",
                        tint = KomolaOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cash on Delivery Order",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AshCharcoal
                            )
                        )
                        Text(
                            text = "Data automatically synced to Google Sheet #3",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cod_modal_close")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(16.dp))

                // Order Summary Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFF8F3),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Order Summary:",
                            fontWeight = FontWeight.Bold,
                            color = AshCharcoal,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (singleProduct != null) {
                            Text(
                                text = "• ${singleProduct.name} (x$quantity)",
                                fontSize = 13.sp,
                                color = Color(0xFF333333)
                            )
                        } else {
                            cartItems.forEach {
                                Text(
                                    text = "• ${it.product.name} (x${it.quantity})",
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Total Payable (COD): ৳${totalAmount.toInt()}",
                            fontWeight = FontWeight.Black,
                            color = KomolaOrange,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Customer Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cod_input_name"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Customer Phone Input
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    placeholder = { Text("01754441155") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cod_input_phone"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Delivery Address Input
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Full Delivery Address *") },
                    placeholder = { Text("House, Road, Area, Thana") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cod_input_address"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // City / District Input
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City / District *") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cod_input_city"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Delivery Instructions / Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Delivery Instructions (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cod_input_notes"),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Order Button
                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank() || address.isBlank() || city.isBlank()) {
                            errorMessage = "Please fill in all required fields (*)."
                        } else {
                            errorMessage = ""
                            onSubmitOrder(name, phone, address, city, notes)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_cod_order_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KomolaOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Confirm Order (Cash on Delivery)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
