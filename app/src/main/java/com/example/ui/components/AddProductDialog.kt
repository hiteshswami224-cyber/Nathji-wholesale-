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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProductEntity
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfit
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onSaveProduct: (
        sku: String,
        title: String,
        msrp: Double,
        category: String,
        subcategory: String,
        size: String,
        caption: String,
        shortDesc: String,
        longDesc: String,
        initialStock: Int,
        boxSize: Int,
        cartonSize: Int
    ) -> Unit
) {
    var sku by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var msrpText by remember { mutableStateOf("10") }
    var category by remember { mutableStateOf("Biscuits") }
    var subcategory by remember { mutableStateOf("Cream Biscuits") }
    var size by remember { mutableStateOf("50g Pack") }
    var caption by remember { mutableStateOf("") }
    var shortDesc by remember { mutableStateOf("") }
    var longDesc by remember { mutableStateOf("") }
    var initialStockText by remember { mutableStateOf("144") }
    var boxSizeText by remember { mutableStateOf("24") }
    var cartonSizeText by remember { mutableStateOf("72") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .testTag("add_product_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AddBusiness, contentDescription = null, tint = MerchantBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Product to Warehouse",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_add_product_btn")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Owner Admin: Create a new SKU, wholesale pricing, and initial stock batch",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEE2E2))
                            .padding(8.dp)
                    ) {
                        Text(text = errorMessage!!, color = Color(0xFFDC2626), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // SKU Code & Title
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sku,
                        onValueChange = { sku = it.uppercase() },
                        label = { Text("SKU Code (e.g. KRN-NP-05) *") },
                        placeholder = { Text("KRN-XX-00") },
                        modifier = Modifier.weight(1f).testTag("new_product_sku_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                    )

                    OutlinedTextField(
                        value = msrpText,
                        onValueChange = { msrpText = it.filter { char -> char.isDigit() || char == '.' } },
                        label = { Text("MSRP (MRP ₹) *") },
                        placeholder = { Text("10.0") },
                        modifier = Modifier.weight(0.7f).testTag("new_product_msrp_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Product Title (प्रोडक्ट का नाम) *") },
                    placeholder = { Text("e.g. Krown Butter Cracker Deluxe") },
                    modifier = Modifier.fillMaxWidth().testTag("new_product_title_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category & Subcategory
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = subcategory,
                        onValueChange = { subcategory = it },
                        label = { Text("Subcategory *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Pack Size & Initial Warehouse Stock
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = size,
                        onValueChange = { size = it },
                        label = { Text("Pack Size (e.g. 50g Pack) *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = initialStockText,
                        onValueChange = { initialStockText = it.filter { char -> char.isDigit() } },
                        label = { Text("Initial Stock (Units) *") },
                        modifier = Modifier.weight(1f).testTag("new_product_stock_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Box & Carton Multipliers
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = boxSizeText,
                        onValueChange = { boxSizeText = it.filter { char -> char.isDigit() } },
                        label = { Text("Units per Box") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = cartonSizeText,
                        onValueChange = { cartonSizeText = it.filter { char -> char.isDigit() } },
                        label = { Text("Units per Master Carton") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    label = { Text("Hindi Marketing Tagline (हिंदी स्लोगन)") },
                    placeholder = { Text("e.g. असली मक्खन का कुरकुरा स्वाद...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = longDesc,
                    onValueChange = { longDesc = it },
                    label = { Text("Full Description for Retailers") },
                    placeholder = { Text("Quality ingredients, baked crisp with rich taste.") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val parsedMsrp = msrpText.toDoubleOrNull()
                        val parsedStock = initialStockText.toIntOrNull() ?: 0
                        val parsedBox = boxSizeText.toIntOrNull() ?: 24
                        val parsedCarton = cartonSizeText.toIntOrNull() ?: 72

                        when {
                            sku.isBlank() -> errorMessage = "Please enter a valid SKU code (e.g. KRN-XX-05)"
                            title.isBlank() -> errorMessage = "Please enter a product title"
                            parsedMsrp == null || parsedMsrp <= 0.0 -> errorMessage = "Please enter a valid MSRP (₹)"
                            else -> {
                                errorMessage = null
                                onSaveProduct(
                                    sku.trim(),
                                    title.trim(),
                                    parsedMsrp,
                                    category.trim().ifBlank { "Biscuits" },
                                    subcategory.trim().ifBlank { "Crackers" },
                                    size.trim().ifBlank { "50g Pack" },
                                    caption.trim(),
                                    title.trim(),
                                    longDesc.trim().ifBlank { "Delicious wholesale packaged FMCG snack." },
                                    parsedStock,
                                    parsedBox,
                                    parsedCarton
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_new_product_btn"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                ) {
                    Text(text = "Add Product to Warehouse Catalog", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun DeleteProductDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onConfirmDelete: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).testTag("delete_product_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Delete Product SKU?", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFFDC2626))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Are you sure you want to remove '${product.title}' (${product.sku}) from the warehouse catalog?",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Current Stock: ${product.currentStock} units will be removed. Customers will no longer be able to purchase this product.",
                        fontSize = 11.sp,
                        color = Color(0xFFB91C1C),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onConfirmDelete(product.sku) },
                        modifier = Modifier.weight(1f).height(42.dp).testTag("confirm_delete_product_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Delete SKU", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
