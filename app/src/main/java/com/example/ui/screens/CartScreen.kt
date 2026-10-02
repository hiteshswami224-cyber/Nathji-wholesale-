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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
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
import com.example.data.model.EnrichedCartItem
import com.example.data.model.SavedAddressEntity
import com.example.data.model.SavedPaymentMethodEntity
import com.example.data.model.WholesalePricingHelper
import com.example.ui.CartSummary
import com.example.ui.components.BulkTierBadge
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfit
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun CartScreen(
    cartItems: List<EnrichedCartItem>,
    summary: CartSummary,
    savedAddresses: List<SavedAddressEntity>,
    savedPaymentMethods: List<SavedPaymentMethodEntity>,
    selectedAddressId: Long?,
    selectedPaymentMethodId: Long?,
    onSelectAddress: (Long) -> Unit,
    onSelectPaymentMethod: (Long) -> Unit,
    onQuantityChange: (sku: String, newQty: Int) -> Unit,
    onRemoveItem: (sku: String) -> Unit,
    onClearCart: () -> Unit,
    onPlaceOrder: () -> Unit,
    onBrowseCatalog: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MerchantBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = MerchantBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Wholesale Cart is Empty",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Add boxes or master cartons from the FMCG catalog to unlock tiered wholesale pricing.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onBrowseCatalog,
                    colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("empty_cart_browse_btn")
                ) {
                    Text(text = "Explore Wholesale Catalog", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Wholesale Cart (${summary.totalUnits} Units)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${cartItems.size} SKUs • Dynamic Bulk Tier Rates Applied",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = onClearCart,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Clear", fontSize = 11.sp)
                }
            }
        }

        // Cart items
        items(cartItems, key = { it.product.sku }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // SKU, Title & Tier Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${item.product.sku} • ${item.product.size} • MRP ₹${item.product.msrp.toInt()}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { onRemoveItem(item.product.sku) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tier Badge & Unit rate
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BulkTierBadge(tier = item.tierInfo)

                        Text(
                            text = "${WholesalePricingHelper.formatCurrency(item.unitPrice)} / pc",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MerchantBlue
                        )
                    }

                    // Next Tier Upsell Hint
                    if (item.nextTierTarget != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(AmberContainer.copy(alpha = 0.7f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "💡 Add ${item.nextTierTarget.neededUnits} more to unlock ${item.nextTierTarget.targetTierName} (${item.nextTierTarget.newDiscountPercent.toInt()}% Margin)!",
                                fontSize = 10.sp,
                                color = Color(0xFF92400E),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stepper + Total line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Stepper
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onQuantityChange(item.product.sku, item.quantity - 12) },
                                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFF1F5F9))
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${item.quantity} pcs",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MerchantBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { onQuantityChange(item.product.sku, item.quantity + 12) },
                                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFF1F5F9))
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                            }
                        }

                        // Line totals
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = WholesalePricingHelper.formatCurrency(item.totalPrice),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "+${WholesalePricingHelper.formatCurrency(item.totalProfit)} profit",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldProfitDark
                            )
                        }
                    }
                }
            }
        }

        // B2B Shipping Address Selector Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = MerchantBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Delivery Shipping Address", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (savedAddresses.isEmpty()) {
                        Text(text = "Using default storefront address in Jaipur, RJ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        savedAddresses.forEach { addr ->
                            val isSelected = (selectedAddressId != null && addr.id == selectedAddressId) || (selectedAddressId == null && addr.isDefault)
                            Surface(
                                onClick = { onSelectAddress(addr.id) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MerchantBlueContainer else Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, if (isSelected) MerchantBlue else BorderLight),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = addr.label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            if (addr.isDefault) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(AmberContainer).padding(horizontal = 4.dp, vertical = 1.dp)
                                                ) {
                                                    Text(text = "DEFAULT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = AmberCarton)
                                                }
                                            }
                                        }
                                        Text(text = "${addr.streetAddress}, ${addr.city} (${addr.pincode})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Selected", tint = MerchantBlue, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // B2B Payment Method Selector Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Payment, contentDescription = null, tint = MerchantBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Wholesale Payment Method", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    savedPaymentMethods.forEach { method ->
                        val isSelected = (selectedPaymentMethodId != null && method.id == selectedPaymentMethodId) || (selectedPaymentMethodId == null && method.isDefault)
                        Surface(
                            onClick = { onSelectPaymentMethod(method.id) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MerchantBlueContainer else Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, if (isSelected) MerchantBlue else BorderLight),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = method.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(text = method.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (isSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Selected", tint = MerchantBlue, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Order Bill & Margin Economics Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Wholesale Bill & Margin Breakdown", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "MSRP Retail Value (${summary.totalUnits} pcs):", fontSize = 12.sp)
                        Text(text = WholesalePricingHelper.formatCurrency(summary.totalRetailValue), fontSize = 12.sp)
                    }

                    val totalDiscount = summary.totalRetailValue - summary.totalWholesaleAmount
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Bulk Wholesale Discount:", fontSize = 12.sp, color = EmeraldProfitDark)
                        Text(text = "-${WholesalePricingHelper.formatCurrency(totalDiscount)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = EmeraldProfitDark)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Estimated B2B Freight (Delhivery):", fontSize = 12.sp)
                        Text(text = "FREE B2B Delivery", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldProfitDark)
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = BorderLight)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Net Wholesale Payable:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            text = WholesalePricingHelper.formatCurrency(summary.totalWholesaleAmount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MerchantBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Profit Highlight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Shop's Net Profit:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldProfitDark
                            )
                            Text(
                                text = "+${WholesalePricingHelper.formatCurrency(summary.totalRetailerProfit)} (${summary.averageMarginPercent.toInt()}%)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldProfitDark
                            )
                        }
                    }
                }
            }
        }

        // Place Order Button
        item {
            Button(
                onClick = onPlaceOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("place_wholesale_order_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
            ) {
                Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm Order (${summary.totalUnits} units • ${WholesalePricingHelper.formatCurrency(summary.totalWholesaleAmount)})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
