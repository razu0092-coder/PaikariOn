package com.example.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.model.Category
import com.example.model.HeaderTheme
import com.example.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.AshCharcoal
import com.example.ui.theme.KomolaOrange

@Composable
fun AllProductsPage(
    products: List<Product>,
    categories: List<Category>,
    selectedCategory: String,
    headerTheme: HeaderTheme,
    onCategorySelect: (String) -> Unit,
    onProductClick: (Product) -> Unit,
    onSelectHeaderTheme: (HeaderTheme) -> Unit
) {
    val scrollState = rememberScrollState()

    val allCategoryNames = listOf("All") + categories.map { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF6F7FA))
            .padding(16.dp)
            .testTag("all_products_page")
    ) {
        // Page Title & Header Theme Selector Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Product Catalog",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = AshCharcoal
                            )
                        )
                        Text(
                            text = "Synced with Google Sheet #2 (Inventory)",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Surface(
                        color = KomolaOrange.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${products.size} Items",
                            color = KomolaOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // COLOR CHOICE OPTION BOX (Ash BG vs Komola BG)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF8F9FB),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ColorLens,
                                contentDescription = "Header Color Choice",
                                tint = KomolaOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Header Theme BG:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AshCharcoal
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Choice 1: Ash BG
                            Surface(
                                onClick = { onSelectHeaderTheme(HeaderTheme.ASH_BG) },
                                color = if (headerTheme == HeaderTheme.ASH_BG) AshCharcoal else Color.LightGray.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("theme_choice_ash")
                            ) {
                                Text(
                                    text = "Ash Charcoal",
                                    color = if (headerTheme == HeaderTheme.ASH_BG) Color.White else AshCharcoal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            // Choice 2: Komola Orange BG
                            Surface(
                                onClick = { onSelectHeaderTheme(HeaderTheme.KOMOLA_BG) },
                                color = if (headerTheme == HeaderTheme.KOMOLA_BG) KomolaOrange else Color.LightGray.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("theme_choice_komola")
                            ) {
                                Text(
                                    text = "Komola Orange",
                                    color = if (headerTheme == HeaderTheme.KOMOLA_BG) Color.White else AshCharcoal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CATEGORY FILTER PILLS
        Text(
            text = "Filter By Category:",
            fontWeight = FontWeight.Bold,
            color = AshCharcoal,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(allCategoryNames) { category ->
                val isSelected = (category == selectedCategory)
                Surface(
                    onClick = { onCategorySelect(category) },
                    color = if (isSelected) KomolaOrange else Color.White,
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = if (isSelected) 3.dp else 1.dp,
                    modifier = Modifier.testTag("cat_pill_$category")
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else AshCharcoal,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PRODUCTS LIST / GRID
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Empty",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No products found matching category '$selectedCategory'",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in products.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProductCard(
                            product = products[i],
                            onProductClick = onProductClick,
                            onAddToCart = {},
                            modifier = Modifier.weight(1f)
                        )

                        if (i + 1 < products.size) {
                            ProductCard(
                                product = products[i + 1],
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

        Spacer(modifier = Modifier.height(24.dp))
    }
}
