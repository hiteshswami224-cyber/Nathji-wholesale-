package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductEntity
import com.example.data.model.WholesalePricingHelper
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun ProductCard(
    product: ProductEntity,
    cartQuantity: Int,
    isOwnerView: Boolean = false,
    onCardClick: () -> Unit,
    onAddLoose: () -> Unit,
    onAddBox: () -> Unit,
    onAddCarton: () -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onOwnerAdjustStock: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val superWholesalePrice = WholesalePricingHelper.calculateUnitPrice(product.msrp, 216)
    val samplePrice = WholesalePricingHelper.calculateUnitPrice(product.msrp, 1)
    val isOutOfStock = product.currentStock <= 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .testTag("product_card_${product.sku}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Category, Subcategory & SKU badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MerchantBlueContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.sku,
                            color = MerchantBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${product.category} • ${product.subcategory}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }

                // Live Stock Badge (Private for customer, detailed for owner)
                StockBadge(
                    currentStock = product.currentStock,
                    minThreshold = product.minStockThreshold,
                    masterCartonSize = product.masterCartonSize,
                    isOwnerView = isOwnerView
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title and Size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.size,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // MSRP tag
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "MSRP (MRP)",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = WholesalePricingHelper.formatShortCurrency(product.msrp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Hindi caption snippet
            if (product.caption.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7).copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "“${product.caption}”",
                        fontSize = 11.sp,
                        color = Color(0xFF92400E),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wholesale Bulk Pricing & Margin Highlight
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Wholesale Bulk Rate",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${WholesalePricingHelper.formatCurrency(superWholesalePrice)} - ${WholesalePricingHelper.formatCurrency(samplePrice)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MerchantBlue
                            )
                            Text(
                                text = " / pack",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Profit Margin badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Up to 40% Margin",
                            color = EmeraldProfitDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isOwnerView) {
                // Owner Action: Direct stock adjustment shortcut
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Warehouse Stock: ${product.currentStock} pcs (${product.currentStock / product.masterCartonSize} Ctn)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MerchantBlue
                    )

                    Button(
                        onClick = { onOwnerAdjustStock?.invoke() },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add / Less Stock", fontSize = 10.sp)
                    }
                }
            } else {
                // Customer Cart Actions
                if (isOutOfStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEE2E2))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Temporarily Out of Stock (वर्तमान में अनुपलब्ध)",
                            color = Color(0xFFB91C1C),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else if (cartQuantity > 0) {
                    // In Cart Counter + Carton shortcut
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MerchantBlue),
                            color = MerchantBlueContainer
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = onDecrement,
                                    modifier = Modifier.size(36.dp).testTag("decrement_${product.sku}")
                                ) {
                                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = MerchantBlue, modifier = Modifier.size(18.dp))
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 8.dp)) {
                                    Text(text = "$cartQuantity pcs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MerchantBlue)
                                    val currentTier = WholesalePricingHelper.getTierForQuantity(cartQuantity)
                                    Text(text = currentTier.name, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                IconButton(
                                    onClick = onIncrement,
                                    modifier = Modifier.size(36.dp).testTag("increment_${product.sku}")
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = MerchantBlue, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = onAddCarton,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AmberCarton),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberCarton),
                            modifier = Modifier.height(40.dp).testTag("quick_carton_${product.sku}")
                        ) {
                            Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "+72 (1 Ctn)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onAddBox,
                            modifier = Modifier.weight(1f).height(42.dp).testTag("add_box_${product.sku}"),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BorderLight),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(text = "+24 (Box)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onAddCarton,
                            modifier = Modifier.weight(1.3f).height(42.dp).testTag("add_carton_${product.sku}"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue, contentColor = Color.White)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "+72 (Carton)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
