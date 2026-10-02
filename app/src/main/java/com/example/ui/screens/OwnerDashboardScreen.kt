package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.WholesalePricingHelper
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
fun OwnerDashboardScreen(
    orders: List<OrderEntity>,
    lowStockProducts: List<ProductEntity>,
    stats: InventoryStats,
    onOpenOrderTracking: (OrderEntity) -> Unit,
    onOpenInvoice: (OrderEntity) -> Unit,
    onAdvanceOrderStatus: (orderId: String, newStatus: String, note: String) -> Unit,
    onOpenStockAdjust: (ProductEntity) -> Unit,
    onNavigateToInventory: () -> Unit,
    onSwitchToCustomerView: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingDispatches = orders.count { it.status != "Delivered" }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Owner Console Hero Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("owner_hero_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = AmberCarton, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "NATHJI WHOLESALE", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Owner Admin Console (मालिक / वेयरहाउस प्रबंधन)", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = onSwitchToCustomerView,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, AmberCarton),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberCarton),
                            modifier = Modifier.height(34.dp).testTag("preview_customer_portal_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "View as Customer", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 KPI Summary Metric Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "TOTAL ORDERS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "${orders.size}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(text = "PENDING DISPATCH", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "$pendingDispatches Orders", color = Color(0xFFFBBF24), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(text = "WAREHOUSE UNITS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "${stats.totalUnitsInStock} pcs", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "STOCK VALUATION", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = WholesalePricingHelper.formatCurrency(stats.totalInventoryValue), color = Color(0xFF6EE7B7), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Low Stock Action Alerts for Owner
        if (lowStockProducts.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = BorderStroke(1.dp, Color(0xFFFECACA))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Low Stock Attention Needed (${lowStockProducts.size} SKUs)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                            }

                            OutlinedButton(
                                onClick = onNavigateToInventory,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(30.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text(text = "Manage All Stock", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        lowStockProducts.take(3).forEach { prod ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = prod.title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                    Text(text = "Stock: ${prod.currentStock} pcs (Threshold: ${prod.minStockThreshold} pcs)", fontSize = 11.sp, color = Color(0xFFB91C1C))
                                }

                                Button(
                                    onClick = { onOpenStockAdjust(prod) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldProfit),
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "+ Add Stock", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Retailer Wholesale Orders Management
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Customer Retailer Orders (${orders.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Manage order dispatches, carrier AWB & automated customer alerts", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        items(orders, key = { it.orderId }) { order ->
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
            val formattedDate = sdf.format(Date(order.timestamp))

            val (statusBg, statusFg) = when (order.status) {
                "Delivered" -> Pair(Color(0xFFDCFCE7), EmeraldProfitDark)
                "Out for Delivery" -> Pair(Color(0xFFFEF3C7), AmberCarton)
                "In Transit" -> Pair(Color(0xFFDBEAFE), Color(0xFF1D4ED8))
                "Dispatched" -> Pair(Color(0xFFE0E7FF), Color(0xFF4338CA))
                else -> Pair(Color(0xFFF1F5F9), Color(0xFF475569))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header: Order ID & Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(statusBg).padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = order.status.uppercase(Locale.US), color = statusFg, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(text = "Placed: $formattedDate", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Text(
                            text = WholesalePricingHelper.formatCurrency(order.totalAmount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MerchantBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Retailer Info
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "BUYER: ${order.retailerName}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = "Delivery to: ${order.retailerAddress}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "Phone: ${order.recipientPhone} • Payment: ${order.paymentMethod}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "Carrier: ${order.carrierName} (AWB: ${order.carrierTrackingNumber})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MerchantBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Items: ${order.totalItemsCount} units", fontSize = 12.sp, fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = BorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Owner Quick Dispatch Status Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (order.status == "Processing") {
                            Button(
                                onClick = {
                                    onAdvanceOrderStatus(order.orderId, "Dispatched", "Dispatched from NATHJI Central Warehouse with Delhivery B2B.")
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                                modifier = Modifier.weight(1.3f).height(38.dp)
                            ) {
                                Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Dispatch & Send SMS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (order.status == "Dispatched" || order.status == "Packed & Staged") {
                            Button(
                                onClick = {
                                    onAdvanceOrderStatus(order.orderId, "In Transit", "Departed Regional Sorting Hub (Jaipur North).")
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                                modifier = Modifier.weight(1.3f).height(38.dp)
                            ) {
                                Text(text = "Mark In Transit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (order.status == "In Transit") {
                            Button(
                                onClick = {
                                    onAdvanceOrderStatus(order.orderId, "Out for Delivery", "Assigned to local delivery vehicle for shop delivery today.")
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberCarton),
                                modifier = Modifier.weight(1.3f).height(38.dp)
                            ) {
                                Text(text = "Mark Out for Delivery", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (order.status == "Out for Delivery") {
                            Button(
                                onClick = {
                                    onAdvanceOrderStatus(order.orderId, "Delivered", "Delivered to shop and POD verified.")
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldProfit),
                                modifier = Modifier.weight(1.3f).height(38.dp)
                            ) {
                                Text(text = "Mark Delivered", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Box(
                                modifier = Modifier.weight(1.3f).clip(RoundedCornerShape(6.dp)).background(Color(0xFFDCFCE7)).padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "✓ Order Fulfilled", color = EmeraldProfitDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = { onOpenOrderTracking(order) },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Text(text = "Tracking", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { onOpenInvoice(order) },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(0.9f).height(38.dp)
                        ) {
                            Text(text = "Invoice", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
