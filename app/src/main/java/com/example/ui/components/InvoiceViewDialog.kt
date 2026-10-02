package com.example.ui.components

import android.content.Intent
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.OrderEntity
import com.example.data.model.WholesalePricingHelper
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InvoiceViewDialog(
    order: OrderEntity,
    onDismiss: () -> Unit,
    onOpenTracking: () -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.US)
    val formattedDate = sdf.format(Date(order.timestamp))

    val taxableAmount = order.totalAmount / 1.05
    val gstAmount = order.totalAmount - taxableAmount
    val cgst = gstAmount / 2.0
    val sgst = gstAmount / 2.0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .testTag("invoice_dialog"),
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
                // Top header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MerchantBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "B2B TAX INVOICE",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_invoice_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Invoice metadata box
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Invoice No: ${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = "Date: $formattedDate", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Carrier: ${order.carrierName}", fontSize = 11.sp)
                            Text(text = "AWB: ${order.carrierTrackingNumber}", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = MerchantBlue)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Payment: ${order.paymentMethod} • Status: ${order.status}", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Seller & Buyer 2-column info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Seller
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "SUPPLIER / DISTRIBUTOR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MerchantBlue)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "NATHJI WHOLESALE FMCG Hub", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(text = "GSTIN: 08AAACD9921E1Z3", fontSize = 10.sp)
                            Text(text = "Central Depot, Jaipur, RJ", fontSize = 10.sp)
                        }
                    }

                    // Buyer
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "BILLED TO (BUYER)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MerchantBlue)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = order.retailerName, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(text = "GSTIN: ${order.retailerGstin}", fontSize = 10.sp)
                            Text(text = order.retailerAddress, fontSize = 10.sp, maxLines = 2)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Itemized List
                Text(
                    text = "Carton & Item Breakdown",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        order.itemsSummary.lines().filter { it.isNotBlank() }.forEach { line ->
                            Text(
                                text = line,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF1E293B),
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Financial Summary
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Taxable Base Amount:", fontSize = 12.sp)
                            Text(text = WholesalePricingHelper.formatCurrency(taxableAmount), fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "CGST (2.5%):", fontSize = 12.sp)
                            Text(text = WholesalePricingHelper.formatCurrency(cgst), fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "SGST (2.5%):", fontSize = 12.sp)
                            Text(text = WholesalePricingHelper.formatCurrency(sgst), fontSize = 12.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 6.dp), color = BorderLight)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Net Wholesale Payable:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = WholesalePricingHelper.formatCurrency(order.totalAmount),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MerchantBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Shopkeeper Retail Value (MRP):", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = WholesalePricingHelper.formatCurrency(order.totalRetailValue), fontSize = 11.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Your Projected Profit Margin:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldProfitDark)
                            Text(
                                text = "+${WholesalePricingHelper.formatCurrency(order.totalRetailerProfit)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = EmeraldProfitDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions: Track Live Shipment + Share Invoice
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Tax Invoice #${order.orderId}")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "NATHJI WHOLESALE Invoice #${order.orderId}\nTotal: ₹${order.totalAmount}\nUnits: ${order.totalItemsCount}\nCarrier: ${order.carrierName} (AWB: ${order.carrierTrackingNumber})\nStatus: ${order.status}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Invoice"))
                        },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Share", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            onDismiss()
                            onOpenTracking()
                        },
                        modifier = Modifier.weight(1.3f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                    ) {
                        Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Track Shipment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
