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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProductEntity
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfit
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun StockAdjustmentDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onRestock: (sku: String, delta: Int, notes: String) -> Unit,
    onSetDirectStock: (sku: String, newStock: Int, notes: String) -> Unit
) {
    var mode by remember { mutableStateOf("ADD") } // "ADD", "REDUCE", "AUDIT"
    var changeUnits by remember { mutableIntStateOf(product.masterCartonSize) }
    var directStockInput by remember { mutableStateOf(product.currentStock.toString()) }
    var notesInput by remember { mutableStateOf("") }

    val resultingStock = when (mode) {
        "ADD" -> product.currentStock + changeUnits
        "REDUCE" -> (product.currentStock - changeUnits).coerceAtLeast(0)
        else -> (directStockInput.toIntOrNull() ?: product.currentStock).coerceAtLeast(0)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .testTag("stock_adjustment_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, tint = MerchantBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Inventory Stock Management", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_stock_dialog")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${product.title} (${product.sku})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = "Current Warehouse Stock: ${product.currentStock} units (${product.currentStock / product.masterCartonSize} Cartons)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mode toggle (Add / Reduce / Audit)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = {
                            mode = "ADD"
                            notesInput = "New factory shipment received"
                        },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mode == "ADD") EmeraldProfit else Color(0xFFF1F5F9),
                            contentColor = if (mode == "ADD") Color.White else Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "+ Add Stock", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            mode = "REDUCE"
                            notesInput = "Damaged / Sample cartons write-off"
                        },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mode == "REDUCE") Color(0xFFDC2626) else Color(0xFFF1F5F9),
                            contentColor = if (mode == "REDUCE") Color.White else Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "- Less Stock", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            mode = "AUDIT"
                            notesInput = "Shelf audit count"
                        },
                        modifier = Modifier.weight(1f).height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mode == "AUDIT") MerchantBlue else Color(0xFFF1F5F9),
                            contentColor = if (mode == "AUDIT") Color.White else Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Audit Count", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (mode == "ADD" || mode == "REDUCE") {
                    Text(
                        text = if (mode == "ADD") "Select Quantity to Add (+)" else "Select Quantity to Reduce (-)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(24 to "24 (Box)", 72 to "72 (Ctn)", 144 to "144", 288 to "288").forEach { (qty, label) ->
                            OutlinedButton(
                                onClick = { changeUnits = qty },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, if (changeUnits == qty) MerchantBlue else BorderLight),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (changeUnits == qty) MerchantBlueContainer else Color.Transparent
                                )
                            ) {
                                Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { changeUnits = (changeUnits - 12).coerceAtLeast(1) },
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFE2E8F0))
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = if (mode == "ADD") "+$changeUnits units" else "-$changeUnits units",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (mode == "ADD") EmeraldProfit else Color(0xFFDC2626)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = { changeUnits += 12 },
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFE2E8F0))
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                } else {
                    Text(text = "Enter Physical Count Found on Warehouse Shelf", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = directStockInput,
                        onValueChange = { directStockInput = it.filter { char -> char.isDigit() } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Exact Count Units") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Calculated balance card
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "New Resulting Stock:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text(
                            text = "$resultingStock units (${resultingStock / product.masterCartonSize} Cartons)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MerchantBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notesInput,
                    onValueChange = { notesInput = it },
                    label = { Text("Reason / Lot Reference Note") },
                    placeholder = { Text("e.g., Factory Batch #91, Damaged in transit") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val finalNote = notesInput.ifBlank {
                            when (mode) {
                                "ADD" -> "Stock intake batch"
                                "REDUCE" -> "Stock reduction / damage write-off"
                                else -> "Physical audit count"
                            }
                        }
                        if (mode == "ADD") {
                            onRestock(product.sku, changeUnits, finalNote)
                        } else if (mode == "REDUCE") {
                            onRestock(product.sku, -changeUnits, finalNote)
                        } else {
                            val newStock = directStockInput.toIntOrNull() ?: product.currentStock
                            onSetDirectStock(product.sku, newStock, finalNote)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_stock_btn"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (mode == "REDUCE") Color(0xFFDC2626) else MerchantBlue
                    )
                ) {
                    Text(
                        text = if (mode == "ADD") "Add to Stock (+)" else if (mode == "REDUCE") "Reduce Stock (-)" else "Save Audit Count",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
