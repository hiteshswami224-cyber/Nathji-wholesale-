package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProductEntity
import com.example.data.model.WholesalePricingHelper
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.AmberCartonDark
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfit
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun ProductDetailDialog(
    product: ProductEntity,
    currentCartQty: Int,
    isOwnerView: Boolean = false,
    onDismiss: () -> Unit,
    onAddToCart: (Int) -> Unit
) {
    var selectedQty by remember {
        mutableIntStateOf(if (currentCartQty > 0) currentCartQty else product.boxSize)
    }

    val unitPrice = WholesalePricingHelper.calculateUnitPrice(product.msrp, selectedQty)
    val lineTotal = unitPrice * selectedQty
    val lineRetailValue = product.msrp * selectedQty
    val lineProfit = (lineRetailValue - lineTotal).coerceAtLeast(0.0)
    val marginPercent = if (lineRetailValue > 0) (lineProfit / lineRetailValue) * 100.0 else 0.0
    val activeTier = WholesalePricingHelper.getTierForQuantity(selectedQty)
    val isOutOfStock = product.currentStock <= 0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .testTag("product_detail_dialog"),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Top Bar with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MerchantBlueContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = product.sku,
                            color = MerchantBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_detail_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title & Size
                Text(
                    text = product.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${product.category} • ${product.subcategory} • ${product.size}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                // Stock availability info
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = MerchantBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isOwnerView) "Warehouse Stock:" else "Stock Status (स्टॉक स्थिति):",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        StockBadge(
                            currentStock = product.currentStock,
                            minThreshold = product.minStockThreshold,
                            masterCartonSize = product.masterCartonSize,
                            isOwnerView = isOwnerView
                        )
                    }
                }

                // Hindi Tagline & Description
                if (product.caption.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "“${product.caption}”",
                            color = Color(0xFF92400E),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.longDescription,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = BorderLight)
                Spacer(modifier = Modifier.height(12.dp))

                // Bulk Pricing Tiers Table
                Text(
                    text = "B2B Bulk Pricing Matrix",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Larger orders unlock higher profit margins for your shop",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Matrix rows
                WholesalePricingHelper.TIERS.forEach { tier ->
                    val tierPrice = WholesalePricingHelper.calculateUnitPrice(product.msrp, tier.minUnits)
                    val isCurrent = tier == activeTier
                    val tierProfitPerUnit = product.msrp - tierPrice

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCurrent) MerchantBlueContainer else Color(0xFFF8FAFC),
                        border = BorderStroke(
                            1.dp,
                            if (isCurrent) MerchantBlue else BorderLight
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.2f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = tier.name,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (isCurrent) MerchantBlue else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "• ACTIVE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MerchantBlue
                                        )
                                    }
                                }
                                Text(
                                    text = tier.description,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${WholesalePricingHelper.formatCurrency(tierPrice)}/pc",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "+${WholesalePricingHelper.formatCurrency(tierProfitPerUnit)} profit (${tier.discountPercent.toInt()}%)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldProfitDark
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = BorderLight)
                Spacer(modifier = Modifier.height(12.dp))

                // Quantity selector
                Text(
                    text = "Select Order Quantity",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        product.boxSize to "+24 (Box)",
                        product.masterCartonSize to "+72 (Ctn)",
                        product.masterCartonSize * 2 to "+144 (2 Ctn)",
                        product.masterCartonSize * 3 to "+216 (Super)"
                    ).forEach { (qty, label) ->
                        OutlinedButton(
                            onClick = {
                                selectedQty = qty.coerceAtMost(product.currentStock)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(
                                1.dp,
                                if (selectedQty == qty) MerchantBlue else BorderLight
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedQty == qty) MerchantBlueContainer else Color.Transparent
                            )
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedQty == qty) MerchantBlue else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stepper Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            selectedQty = (selectedQty - 12).coerceAtLeast(1)
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE2E8F0))
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$selectedQty units",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MerchantBlue
                        )
                        val cartons = selectedQty / product.masterCartonSize
                        val loose = selectedQty % product.masterCartonSize
                        Text(
                            text = "$cartons Master Cartons + $loose packs",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            selectedQty = (selectedQty + 12).coerceAtMost(product.currentStock)
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE2E8F0))
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Real-time calculation summary card
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Wholesale Total ($selectedQty pcs):", fontSize = 12.sp)
                            Text(
                                text = WholesalePricingHelper.formatCurrency(lineTotal),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MerchantBlue
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "MSRP Retail Value:", fontSize = 12.sp)
                            Text(
                                text = WholesalePricingHelper.formatCurrency(lineRetailValue),
                                fontSize = 12.sp
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Your Gross Profit:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldProfitDark
                            )
                            Text(
                                text = "${WholesalePricingHelper.formatCurrency(lineProfit)} (${marginPercent.toInt()}%)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldProfitDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Add to Cart Button
                Button(
                    onClick = {
                        onAddToCart(selectedQty)
                        onDismiss()
                    },
                    enabled = !isOutOfStock && selectedQty > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("detail_add_to_cart_btn"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MerchantBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOutOfStock) "Out of Stock" else "Update Cart ($selectedQty pcs • ${WholesalePricingHelper.formatCurrency(lineTotal)})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
