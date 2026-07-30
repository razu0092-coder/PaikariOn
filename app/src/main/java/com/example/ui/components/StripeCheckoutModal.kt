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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.KeyboardType
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
fun StripeCheckoutModal(
    singleProduct: Product? = null,
    quantity: Int = 1,
    cartItems: List<CartItem> = emptyList(),
    userProfile: UserProfile? = null,
    onDismiss: () -> Unit,
    onSubmitStripePayment: (name: String, phone: String, address: String, city: String, cardNumber: String) -> Unit
) {
    var name by remember { mutableStateOf(userProfile?.name ?: "") }
    var phone by remember { mutableStateOf(userProfile?.phone ?: "") }
    var address by remember { mutableStateOf(userProfile?.address ?: "") }
    var city by remember { mutableStateOf(userProfile?.city?.ifEmpty { "Dhaka" } ?: "Dhaka") }

    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }

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
                .testTag("stripe_checkout_modal"),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Stripe Payment",
                        tint = KomolaOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Stripe Secure Card Checkout",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AshCharcoal
                            )
                        )
                        Text(
                            text = "256-Bit SSL Encrypted Payment Gateway",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("stripe_modal_close")
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

                // Payable Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = AshCharcoal,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Amount to Charge:",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "৳${totalAmount.toInt()}",
                                color = KomolaOrange,
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Secured",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name & Contact
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Cardholder Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stripe_input_name"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stripe_input_phone"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Shipping Address *") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = KomolaOrange) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stripe_input_address"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Card Payment Details:",
                    fontWeight = FontWeight.Bold,
                    color = AshCharcoal,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Card Number Input
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { if (it.length <= 19) cardNumber = it },
                    label = { Text("Card Number (4242 ...)") },
                    placeholder = { Text("4242 4242 4242 4242") },
                    leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = AshCharcoal) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stripe_input_card_num"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Expiry and CVC Row
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { if (it.length <= 5) expiryDate = it },
                        label = { Text("MM/YY") },
                        placeholder = { Text("12/28") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = AshCharcoal) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stripe_input_expiry"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = cvc,
                        onValueChange = { if (it.length <= 4) cvc = it },
                        label = { Text("CVC") },
                        placeholder = { Text("123") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AshCharcoal) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stripe_input_cvc"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

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

                // Submit Payment Button
                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank() || address.isBlank() || cardNumber.isBlank() || expiryDate.isBlank() || cvc.isBlank()) {
                            errorMessage = "Please complete all billing and card information fields."
                        } else {
                            errorMessage = ""
                            onSubmitStripePayment(name, phone, address, city, cardNumber)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_stripe_payment_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AshCharcoal,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Pay ৳${totalAmount.toInt()} via Stripe",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = KomolaOrange
                    )
                }
            }
        }
    }
}
