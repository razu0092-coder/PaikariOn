package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Order
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange
import com.example.ui.theme.SuccessGreen

@Composable
fun OrderSuccessDialog(
    order: Order,
    onDismiss: () -> Unit,
    onTrackOrder: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .testTag("order_success_dialog"),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = SuccessGreen,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Order Placed Successfully!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = AshCharcoal
                    )
                )

                Text(
                    text = "Data synced to Google Sheet (Sheet 3)",
                    fontSize = 12.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Details Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF8F9FA),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Order ID: ", fontSize = 12.sp, color = Color.Gray)
                            Text(text = order.orderId, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AshCharcoal)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Tracking #: ", fontSize = 12.sp, color = Color.Gray)
                            Text(text = order.trackingNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KomolaOrange)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Payment: ", fontSize = 12.sp, color = Color.Gray)
                            Text(text = order.paymentMethod, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AshCharcoal)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Total Paid: ", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "৳${order.totalAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = KomolaOrange)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onTrackOrder()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("track_order_success_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KomolaOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Track Delivery Status", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("continue_shopping_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Continue Shopping", color = AshCharcoal)
                }
            }
        }
    }
}
