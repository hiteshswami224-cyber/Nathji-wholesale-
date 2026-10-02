package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.InventoryLogEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.WholesalePricingHelper
import com.example.ui.InventoryFilter
import com.example.ui.InventoryStats
import com.example.ui.components.StockBadge
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfit
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InventoryScreen(
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    stats: InventoryStats,
    filter: InventoryFilter,
    recentLogs: List<InventoryLogEntity>,
    onFilterChange: (InventoryFilter) -> Unit,
    onAdjustStockClick: (ProductEntity) -> Unit,
    onAddNewProductClick: () -> Unit,
    onDeleteProductClick: (ProductEntity) -> Unit,
    onOpenOrderTracking: (OrderEntity) -> Unit,
    onOpenInvoice: (OrderEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf("STOCK") } // "STOCK", "ORDERS", "LOGS"

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Warehouse Stats Card
        Surface(
            color = Color(0xFF0F172A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Owner Inventory Management",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AmberCarton)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(text = "OWNER ONLY", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            text = "Central FMCG Warehouse • Stock Add/Less & Catalog",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }

                    if (stats.lowStockCount > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEF4444))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${stats.lowStockCount} Low Stock",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats 4-column metric row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "TOTAL UNITS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${stats.totalUnitsInStock} pcs", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(text = "MASTER CTNS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${stats.totalCartonsInStock} Ctn", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(text = "CATALOG SKUS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${stats.totalSkus} SKUs", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "STOCK VALUATION", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = WholesalePricingHelper.formatCurrency(stats.totalInventoryValue), color = Color(0xFF6EE7B7), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Sub-tabs: Live Products vs Total Order History vs Movement Logs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { activeTab = "STOCK" },
                modifier = Modifier.weight(1f).height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "STOCK") MerchantBlue else Color(0xFFE2E8F0),
                    contentColor = if (activeTab == "STOCK") Color.White else Color(0xFF334155)
                )
            ) {
                Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Stock (${products.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { activeTab = "ORDERS" },
                modifier = Modifier.weight(1.1f).height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "ORDERS") MerchantBlue else Color(0xFFE2E8F0),
                    contentColor = if (activeTab == "ORDERS") Color.White else Color(0xFF334155)
                )
            ) {
                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Orders (${orders.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { activeTab = "LOGS" },
                modifier = Modifier.weight(1f).height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == "LOGS") MerchantBlue else Color(0xFFE2E8F0),
                    contentColor = if (activeTab == "LOGS") Color.White else Color(0xFF334155)
                )
            ) {
                Icon(imageVector = Icons.Default.History, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Logs (${recentLogs.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (activeTab == "STOCK") {
            // Action bar: Filter chips + Add New Product Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Pills
                Row(
                    modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InventoryFilter.values().forEach { f ->
                        val selected = filter == f
                        FilterChip(
                            selected = selected,
                            onClick = { onFilterChange(f) },
                            label = { Text(text = f.label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (f == InventoryFilter.LOW_STOCK) Color(0xFFEF4444) else MerchantBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // + Add New Product Button
                Button(
                    onClick = onAddNewProductClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldProfit),
                    modifier = Modifier.height(36.dp).testTag("owner_add_product_btn"),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+ New SKU", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Inventory List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(products, key = { it.sku }) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("inventory_item_${product.sku}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = product.sku, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MerchantBlue)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "${product.category} • ${product.size}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }

                                StockBadge(
                                    currentStock = product.currentStock,
                                    minThreshold = product.minStockThreshold,
                                    masterCartonSize = product.masterCartonSize,
                                    isOwnerView = true
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = BorderLight)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Inventory details & Action
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    val cartons = product.currentStock / product.masterCartonSize
                                    val boxes = product.currentStock / product.boxSize
                                    val stockVal = product.currentStock * product.baseWholesaleCost
                                    Text(
                                        text = "$cartons Cartons ($boxes Boxes) • Val: ${WholesalePricingHelper.formatCurrency(stockVal)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "MSRP: ₹${product.msrp.toInt()} • Min Alert: ${product.minStockThreshold} pcs",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { onAdjustStockClick(product) },
                                        modifier = Modifier.height(34.dp).testTag("adjust_stock_${product.sku}"),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text(text = "+/- Stock", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.width(4.dp))

                                    IconButton(
                                        onClick = { onDeleteProductClick(product) },
                                        modifier = Modifier.size(34.dp).testTag("delete_sku_${product.sku}")
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete SKU", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else if (activeTab == "ORDERS") {
            // TOTAL ORDER HISTORY VIEW
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    // Order History KPI Summary
                    val totalTurnover = orders.sumOf { it.totalAmount }
                    val totalPacks = orders.sumOf { it.totalItemsCount }
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "TOTAL REVENUE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = WholesalePricingHelper.formatCurrency(totalTurnover), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MerchantBlue)
                            }
                            Column {
                                Text(text = "TOTAL UNITS SHIPPED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "$totalPacks pcs", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "LIFETIME ORDERS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "${orders.size} Orders", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                items(orders, key = { it.orderId }) { order ->
                    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Buyer: ${order.retailerName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                Text(
                                    text = WholesalePricingHelper.formatCurrency(order.totalAmount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MerchantBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${order.totalItemsCount} units • Placed: ${sdf.format(Date(order.timestamp))}", fontSize = 11.sp)
                            Text(text = "Status: ${order.status} • Carrier: ${order.carrierName} (AWB: ${order.carrierTrackingNumber})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { onOpenOrderTracking(order) },
                                    modifier = Modifier.weight(1f).height(34.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Live Tracking", fontSize = 10.sp)
                                }

                                OutlinedButton(
                                    onClick = { onOpenInvoice(order) },
                                    modifier = Modifier.weight(1f).height(34.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Tax Invoice", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            // Stock Movement Audit Logs
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentLogs, key = { it.id }) { log ->
                    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (log.changeAmount >= 0) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = log.actionType,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (log.changeAmount >= 0) EmeraldProfitDark else Color(0xFFB91C1C)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = log.sku, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MerchantBlue)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = log.productTitle, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text(text = log.notes, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = sdf.format(Date(log.timestamp)), fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (log.changeAmount >= 0) "+${log.changeAmount}" else "${log.changeAmount}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (log.changeAmount >= 0) EmeraldProfitDark else Color(0xFFB91C1C)
                                )
                                Text(
                                    text = "Balance: ${log.resultingStock} pcs",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
