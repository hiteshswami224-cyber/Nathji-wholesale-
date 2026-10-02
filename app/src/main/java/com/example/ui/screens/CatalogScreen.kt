package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CartItemEntity
import com.example.data.model.ProductEntity
import com.example.ui.SortOption
import com.example.ui.components.ProductCard
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun CatalogScreen(
    products: List<ProductEntity>,
    cartItems: List<CartItemEntity>,
    searchQuery: String,
    selectedCategory: String,
    selectedSubcategory: String,
    sortOption: SortOption,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSubcategoryChange: (String) -> Unit,
    onSortChange: (SortOption) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onAddLoose: (ProductEntity) -> Unit,
    onAddBox: (ProductEntity) -> Unit,
    onAddCarton: (ProductEntity) -> Unit,
    onDecrement: (ProductEntity) -> Unit,
    onIncrement: (ProductEntity) -> Unit,
    isOwnerView: Boolean = false,
    onOwnerAdjustStock: ((ProductEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Biscuits", "Wafers")
    val subcategories = when (selectedCategory) {
        "Biscuits" -> listOf("All", "Cream Biscuits", "Crackers", "Marie Biscuits", "Sweet Biscuits", "Cookies", "Health Biscuits")
        "Wafers" -> listOf("All", "Chocolate Wafers")
        else -> listOf("All", "Cream Biscuits", "Crackers", "Marie Biscuits", "Sweet Biscuits", "Chocolate Wafers", "Cookies", "Health Biscuits")
    }

    var showSortMenu by remember { mutableStateOf(false) }
    val cartQtyMap = remember(cartItems) { cartItems.associate { it.sku to it.quantity } }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Search & Sort bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("catalog_search_input"),
                placeholder = { Text("Search by SKU, Hindi title, or type...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MerchantBlue,
                    unfocusedBorderColor = BorderLight,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Sort button
            Box {
                Surface(
                    onClick = { showSortMenu = true },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "Sort", tint = MerchantBlue)
                    }
                }

                DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                    SortOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.label,
                                    fontWeight = if (sortOption == option) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sortOption == option) MerchantBlue else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onSortChange(option)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Horizontal Category Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categories.forEach { cat ->
                val selected = selectedCategory == cat
                FilterChip(
                    selected = selected,
                    onClick = { onCategoryChange(cat) },
                    label = { Text(text = cat, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MerchantBlue,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_cat_$cat")
                )
            }
        }

        // Horizontal Subcategory Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            subcategories.forEach { sub ->
                val selected = selectedSubcategory == sub
                FilterChip(
                    selected = selected,
                    onClick = { onSubcategoryChange(sub) },
                    label = { Text(text = sub, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberCarton,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_sub_$sub")
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Product List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Promotional Wholesale Banner Item
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = MerchantBlue)
                ) {
                    Column {
                        // Hero image
                        Image(
                            painter = painterResource(id = R.drawable.wholesale_hero),
                            contentDescription = "Wholesale Carton Bonanza",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            contentScale = ContentScale.Crop
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Wholesale Master Carton Bonanza",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Buy 3+ Cartons (216+ pcs) & enjoy guaranteed 40% Retailer Margin",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF59E0B))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "40% OFF", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Products count header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Wholesale FMCG Catalog (${products.size} Products)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Real-time stock updated",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Product Cards
            items(products, key = { it.sku }) { product ->
                val qty = cartQtyMap[product.sku] ?: 0
                ProductCard(
                    product = product,
                    cartQuantity = qty,
                    isOwnerView = isOwnerView,
                    onCardClick = { onProductClick(product) },
                    onAddLoose = { onAddLoose(product) },
                    onAddBox = { onAddBox(product) },
                    onAddCarton = { onAddCarton(product) },
                    onDecrement = { onDecrement(product) },
                    onIncrement = { onIncrement(product) },
                    onOwnerAdjustStock = { onOwnerAdjustStock?.invoke(product) }
                )
            }

            // Bottom spacer for comfortable scrolling
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
