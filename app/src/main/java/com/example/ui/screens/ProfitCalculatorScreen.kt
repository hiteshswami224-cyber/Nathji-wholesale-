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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.ProductEntity
import com.example.data.model.WholesalePricingHelper
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfit
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun ProfitCalculatorScreen(
    products: List<ProductEntity>,
    onAddAssortmentToCart: (List<Pair<ProductEntity, Int>>) -> Unit,
    modifier: Modifier = Modifier
) {
    var investmentBudget by remember { mutableFloatStateOf(10000f) }

    // Calculate simulation numbers
    val averageWholesaleMultiplier = 0.65 // Average 35% discount for bulk
    val projectedRetailRealization = (investmentBudget / averageWholesaleMultiplier).toDouble()
    val netShopProfit = projectedRetailRealization - investmentBudget
    val roiPercent = (netShopProfit / investmentBudget) * 100.0

    // Recommended Cartons assortment based on budget
    val cartonPacks = remember(investmentBudget, products) {
        val count = (investmentBudget / 1000f).toInt().coerceAtLeast(1)
        products.take(6).map { prod ->
            val cartonsCount = (count / 6).coerceAtLeast(1)
            prod to (cartonsCount * prod.masterCartonSize)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MerchantBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kirana Bulk Profit Simulator",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Simulate your shop's wholesale return on investment (ROI). Bulk master carton orders maximize daily retail profits.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Budget Slider Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("calculator_budget_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Wholesale Purchase Investment", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = WholesalePricingHelper.formatCurrency(investmentBudget.toDouble()),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MerchantBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = investmentBudget,
                        onValueChange = { investmentBudget = it },
                        valueRange = 2000f..50000f,
                        steps = 23,
                        colors = SliderDefaults.colors(
                            thumbColor = MerchantBlue,
                            activeTrackColor = MerchantBlue
                        )
                    )

                    // Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(5000f, 10000f, 25000f, 50000f).forEach { preset ->
                            OutlinedButton(
                                onClick = { investmentBudget = preset },
                                modifier = Modifier.weight(1f).height(32.dp),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(text = "₹${(preset / 1000).toInt()}k", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Projected Profit Results Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = EmeraldProfitDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Projected Retail Turnover & Earnings", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "WHOLESALE COST", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = WholesalePricingHelper.formatCurrency(investmentBudget.toDouble()), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Column {
                            Text(text = "RETAIL SALES (MSRP)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = WholesalePricingHelper.formatCurrency(projectedRetailRealization), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MerchantBlue)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "NET SHOP PROFIT", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "+${WholesalePricingHelper.formatCurrency(netShopProfit)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = EmeraldProfitDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "🚀 Profit Margin: ~${roiPercent.toInt()}% gross return on carton stock turnover",
                            fontWeight = FontWeight.Bold,
                            color = EmeraldProfitDark,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Recommended Master Carton Mix
        item {
            Column {
                Text(text = "Optimized Fast-Moving FMCG Assortment", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Best-selling high-margin biscuits & wafers combo for your Kirana shelf", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    cartonPacks.forEach { (prod, qty) ->
                        val cartons = qty / prod.masterCartonSize
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = prod.title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text(text = "${prod.sku} • MRP ₹${prod.msrp.toInt()}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(
                                text = "$cartons Master Ctn ($qty pcs)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MerchantBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onAddAssortmentToCart(cartonPacks) },
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("add_assortment_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                    ) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Load This Combo into Wholesale Cart", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
