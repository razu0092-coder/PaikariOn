package com.example.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.PublishedWithChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange

@Composable
fun OurPolicyPage() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    val policyTabs = listOf(
        "7-Day Return Policy",
        "Cash on Delivery Policy",
        "Privacy Policy",
        "Terms & Conditions"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF6F7FA))
            .padding(16.dp)
            .testTag("our_policy_page")
    ) {
        // Page Title Header
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
                    imageVector = Icons.Default.Policy,
                    contentDescription = "Policy",
                    tint = KomolaOrange,
                    modifier = Modifier.size(32.dp)
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = "Store Policies & Promises",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AshCharcoal
                        )
                    )
                    Text(
                        text = "Clear, transparent terms for every order",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Policy Tabs Header
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = KomolaOrange,
            edgePadding = 0.dp
        ) {
            policyTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.testTag("policy_tab_$index"),
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) KomolaOrange else AshCharcoal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Policy Body Details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                when (selectedTabIndex) {
                    0 -> {
                        // Return Policy
                        PolicySectionHeader(
                            icon = Icons.Default.PublishedWithChanges,
                            title = "7-Day Easy Return & Replacement"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyBulletPoint("1. Defective or Damaged Product: If you receive a damaged or broken product, notify us within 48 hours for instant replacement.")
                        PolicyBulletPoint("2. Wrong Item Delivered: We cover full return shipping costs if the delivered item differs from your order.")
                        PolicyBulletPoint("3. Unopened Condition: Returned products must include original box, tags, and accessories intact.")
                        PolicyBulletPoint("4. Contact Support: Initiate returns via WhatsApp/Call at 01754441155, email at mail.razu0092@gmail.com, or FB page https://www.facebook.com/OnBazar.Daka with your order ID.")
                    }
                    1 -> {
                        // COD Shipping Policy
                        PolicySectionHeader(
                            icon = Icons.Default.LocalShipping,
                            title = "Cash on Delivery & Shipping Policy"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyBulletPoint("1. Dhaka City Delivery: Standard 24 to 48 hours home delivery with Cash on Delivery option.")
                        PolicyBulletPoint("2. Outside Dhaka Delivery: Express courier delivery within 2 to 4 working days.")
                        PolicyBulletPoint("3. Check Before Payment: You are welcome to inspect product packaging at time of delivery.")
                        PolicyBulletPoint("4. Automated Sheet Tracking: Every order generates an instant tracking number synced with Google Sheet #3.")
                    }
                    2 -> {
                        // Privacy Policy
                        PolicySectionHeader(
                            icon = Icons.Default.PrivacyTip,
                            title = "Privacy Policy & Data Handling"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyBulletPoint("1. Data Security: Customer name, phone, and delivery address are stored securely for order fulfillment.")
                        PolicyBulletPoint("2. Google Sheets Integration: Registration & profile details are safely recorded in Google Sheet #1 for order processing.")
                        PolicyBulletPoint("3. Zero Spam: We do not sell or lease your personal information to third-party advertisers.")
                        PolicyBulletPoint("4. Stripe Payment Security: Credit card credentials are handled strictly by Stripe 256-Bit SSL payment servers.")
                    }
                    3 -> {
                        // Terms
                        PolicySectionHeader(
                            icon = Icons.Default.Gavel,
                            title = "Terms & Conditions"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyBulletPoint("1. Price Updates: Prices and stock levels are synchronized in real-time with Google Sheet #2.")
                        PolicyBulletPoint("2. Order Confirmation: Orders are subject to inventory verification prior to dispatch.")
                        PolicyBulletPoint("3. WhatsApp Support: Official store communications occur via 01754441155.")
                        PolicyBulletPoint("4. Customer Satisfaction: We strive for 100% customer happiness on every shipment.")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PolicySectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = KomolaOrange, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AshCharcoal
            )
        )
    }
}

@Composable
private fun PolicyBulletPoint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = Color(0xFF4A5260),
            lineHeight = 20.sp
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
