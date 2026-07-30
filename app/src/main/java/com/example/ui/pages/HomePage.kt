package com.example.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.Category
import com.example.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange
import com.example.viewmodel.AppNavPage
import kotlinx.coroutines.delay

@Composable
fun HomePage(
    topProducts: List<Product>,
    categories: List<Category>,
    onProductClick: (Product) -> Unit,
    onCategoryClick: (String) -> Unit,
    onNavigate: (AppNavPage) -> Unit,
    onWhatsAppCall: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Banners for Slider Section
    val bannerImages = listOf(
        R.drawable.img_hero_banner_1785312766238,
        R.drawable.img_promo_banner_1785312779813
    )

    var currentBannerIndex by remember { mutableIntStateOf(0) }

    // Auto-slide banner effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentBannerIndex = (currentBannerIndex + 1) % bannerImages.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF6F7FA))
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // 1. SLIDER SECTION (Interactive Banner Carousel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("slider_section")
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.9f)
                    .clip(RoundedCornerShape(20.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = bannerImages[currentBannerIndex]),
                        contentDescription = "Promotional Banner Slider",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                )
                            )
                    )

                    // Overlay CTA Text
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            color = KomolaOrange,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "SPECIAL OFFER",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Up to 40% Discount on Top Tech & Fashion",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Carousel Dots Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                bannerImages.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == currentBannerIndex) 20.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentBannerIndex) KomolaOrange else Color.LightGray
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. CATEGORY SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("category_section")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore Categories",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AshCharcoal
                    )
                )
                Text(
                    text = "See All",
                    color = KomolaOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onNavigate(AppNavPage.ALL_PRODUCTS) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(categories) { category ->
                    val icon: ImageVector = when (category.name) {
                        "Electronics" -> Icons.Default.Devices
                        "Fashion" -> Icons.Default.ShoppingBag
                        "Gadgets" -> Icons.Default.Devices
                        "Home & Kitchen" -> Icons.Default.HomeWork
                        "Beauty & Health" -> Icons.Default.Face
                        else -> Icons.Default.ShoppingBag
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onCategoryClick(category.name)
                                onNavigate(AppNavPage.ALL_PRODUCTS)
                            }
                            .testTag("category_item_${category.name.lowercase()}"),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(KomolaOrange.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = category.name,
                                    tint = KomolaOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = category.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AshCharcoal
                                )
                                Text(
                                    text = "${category.itemQuantity}+ Items",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. TOP PRODUCTS SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("top_products_section")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Top Featured Products",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AshCharcoal
                        )
                    )
                    Text(
                        text = "Best-selling products with exclusive discounts",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    onClick = { onNavigate(AppNavPage.ALL_PRODUCTS) },
                    color = KomolaOrange,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "View All",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Products Grid
            val displayProducts = topProducts.take(6)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in displayProducts.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProductCard(
                            product = displayProducts[i],
                            onProductClick = onProductClick,
                            onAddToCart = {},
                            modifier = Modifier.weight(1f)
                        )

                        if (i + 1 < displayProducts.size) {
                            ProductCard(
                                product = displayProducts[i + 1],
                                onProductClick = onProductClick,
                                onAddToCart = {},
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 4. OUR TARGET / MISSION SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AshCharcoal)
                .padding(20.dp)
                .testTag("our_target_section")
        ) {
            Text(
                text = "OUR TARGET & PROMISE",
                color = KomolaOrange,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontSize = 12.sp
            )
            Text(
                text = "Why 50,000+ Customers Trust Our Store",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            val targets = listOf(
                Triple(Icons.Default.LocalShipping, "Fast Express Shipping", "Superfast door-to-door delivery across Bangladesh with live tracking."),
                Triple(Icons.Default.Verified, "100% Quality Guaranteed", "Authentic imported products direct from verified manufacturers."),
                Triple(Icons.Default.CreditCard, "COD & Stripe Secure", "Option for Cash on Delivery or 256-Bit SSL Stripe card checkout."),
                Triple(Icons.Default.SupportAgent, "24/7 WhatsApp Support", "Instant assistance on WhatsApp 01754441155 whenever you need.")
            )

            targets.forEach { (icon, title, desc) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(KomolaOrange.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = KomolaOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = desc, color = Color.LightGray, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick WhatsApp CTA
            Button(
                onClick = onWhatsAppCall,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("home_whatsapp_cta_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Call, contentDescription = "WhatsApp", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Quick WhatsApp Support (01754441155)", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5. FOOTER & ALL RIGHTS RESERVED
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF191D24))
                .padding(20.dp)
                .testTag("footer_section"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PaikariOn",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
            Text(
                text = "\"Not only Product; Get a Better Experience.\"",
                color = KomolaOrange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            Text(
                text = "📍 2/2 Darussalam, Mirpur-1, Dhaka-1216, Dhaka\n📞 01754441155 • ✉️ mail.razu0092@gmail.com\n🌐 fb.com/OnBazar.Daka",
                color = Color.LightGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "About Us", color = KomolaOrange, fontSize = 12.sp, modifier = Modifier.clickable { onNavigate(AppNavPage.ABOUT_US) })
                Text(text = "Our Policy", color = KomolaOrange, fontSize = 12.sp, modifier = Modifier.clickable { onNavigate(AppNavPage.OUR_POLICY) })
                Text(text = "Track Order", color = KomolaOrange, fontSize = 12.sp, modifier = Modifier.clickable { onNavigate(AppNavPage.ORDER_TRACKING) })
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "© 2026 PaikariOn. All Rights Reserved.",
                color = Color.LightGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
