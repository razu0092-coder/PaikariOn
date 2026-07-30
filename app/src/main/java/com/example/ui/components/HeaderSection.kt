package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.HeaderTheme
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange
import com.example.viewmodel.AppNavPage

@Composable
fun HeaderSection(
    headerTheme: HeaderTheme,
    currentPage: AppNavPage,
    searchQuery: String,
    cartItemCount: Int,
    onSearchQueryChange: (String) -> Unit,
    onToggleTheme: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenCart: () -> Unit,
    onNavigate: (AppNavPage) -> Unit
) {
    val headerBgColor by animateColorAsState(
        targetValue = if (headerTheme == HeaderTheme.KOMOLA_BG) KomolaOrange else AshCharcoal,
        label = "HeaderBgColor"
    )

    val contentColor = Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBgColor)
            .padding(top = 12.dp)
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo, Name & Tagline
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Logo Image
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.paikarion_white_logo_1785318128093),
                        contentDescription = "PaikariOn Brand Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "PaikariOn",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = contentColor,
                            letterSpacing = 0.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Not only Product; Get a Better Experience.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = contentColor.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Theme Toggle Button (Ash vs Komola choice)
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier
                    .testTag("header_theme_toggle")
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = "Toggle Header BG Color",
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Registration / Profile Icon
            IconButton(
                onClick = onOpenProfile,
                modifier = Modifier.testTag("header_profile_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Profile & Sheet Registration",
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // CEO Admin Control Access Icon
            IconButton(
                onClick = { onNavigate(AppNavPage.ADMIN_CONTROL) },
                modifier = Modifier.testTag("header_admin_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "CEO Admin Control",
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Cart Icon with Badge Counter
            IconButton(
                onClick = onOpenCart,
                modifier = Modifier.testTag("header_cart_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = if (headerTheme == HeaderTheme.KOMOLA_BG) AshCharcoal else KomolaOrange,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = cartItemCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(contentColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("header_search_input"),
            placeholder = {
                Text(
                    text = "Search products, categories, specs...",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AshCharcoal
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs for Main Site Sections & Admin
        val tabs = listOf(
            AppNavPage.HOME to "Home",
            AppNavPage.ALL_PRODUCTS to "All Products",
            AppNavPage.ORDER_TRACKING to "Track Order",
            AppNavPage.ABOUT_US to "About Us",
            AppNavPage.OUR_POLICY to "Our Policy",
            AppNavPage.CART to "Cart ($cartItemCount)",
            AppNavPage.ADMIN_CONTROL to "Admin"
        )

        ScrollableTabRow(
            selectedTabIndex = tabs.indexOfFirst { it.first == currentPage }.coerceAtLeast(0),
            containerColor = Color.Transparent,
            contentColor = contentColor,
            edgePadding = 12.dp,
            divider = {}
        ) {
            tabs.forEach { (page, label) ->
                val isSelected = (page == currentPage)
                Tab(
                    selected = isSelected,
                    onClick = { onNavigate(page) },
                    modifier = Modifier.testTag("nav_tab_${page.name.lowercase()}"),
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) contentColor else contentColor.copy(alpha = 0.75f)
                            )
                        )
                    }
                )
            }
        }
    }
}
