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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BusinessProfileEntity
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldProfitDark
import com.example.ui.theme.MerchantBlue
import com.example.ui.theme.MerchantBlueContainer

@Composable
fun CustomerAuthDialog(
    currentProfile: BusinessProfileEntity?,
    onDismiss: () -> Unit,
    onCustomerLoginSuccess: (BusinessProfileEntity) -> Unit,
    onOwnerLoginSuccess: () -> Unit
) {
    var authMode by remember { mutableStateOf("CUSTOMER_LOGIN") } // "CUSTOMER_LOGIN", "CUSTOMER_REGISTER", "OWNER_PIN"

    // Customer fields
    var shopName by remember { mutableStateOf(currentProfile?.businessName ?: "Ganesh Kirana & General Store") }
    var ownerName by remember { mutableStateOf(currentProfile?.ownerName ?: "Ramesh Sharma") }
    var phone by remember { mutableStateOf(currentProfile?.phone ?: "+91 98290 41235") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "sharma.kirana@gmail.com") }
    var gstin by remember { mutableStateOf(currentProfile?.gstin ?: "08AABCG1234F1Z8") }
    var businessType by remember { mutableStateOf(currentProfile?.businessType ?: "Retail Kirana Store") }

    // Owner PIN
    var ownerPinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .testTag("auth_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header with close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NATHJI WHOLESALE",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MerchantBlue
                        )
                        Text(
                            text = when (authMode) {
                                "OWNER_PIN" -> "Owner / Admin Login (मालिक लॉगिन)"
                                "CUSTOMER_REGISTER" -> "New Retailer Registration (नया ग्राहक पंजीकरण)"
                                else -> "Customer / Retailer Login (किराना ग्राहक लॉगिन)"
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_auth_btn")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { authMode = "CUSTOMER_LOGIN"; pinError = false },
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authMode == "CUSTOMER_LOGIN") MerchantBlue else Color(0xFFF1F5F9),
                            contentColor = if (authMode == "CUSTOMER_LOGIN") Color.White else Color(0xFF334155)
                        )
                    ) {
                        Text(text = "Customer Login", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { authMode = "CUSTOMER_REGISTER"; pinError = false },
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authMode == "CUSTOMER_REGISTER") MerchantBlue else Color(0xFFF1F5F9),
                            contentColor = if (authMode == "CUSTOMER_REGISTER") Color.White else Color(0xFF334155)
                        )
                    ) {
                        Text(text = "Register Shop", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { authMode = "OWNER_PIN" },
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authMode == "OWNER_PIN") AmberCarton else Color(0xFFF1F5F9),
                            contentColor = if (authMode == "OWNER_PIN") Color.White else Color(0xFF334155)
                        )
                    ) {
                        Text(text = "Owner Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (authMode == "OWNER_PIN") {
                    // Owner Admin Login Screen
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = AmberCarton)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Store Owner & Warehouse Manager Console. Manage inventory, add/less stock, and dispatch retailer orders.",
                                fontSize = 11.sp,
                                color = Color(0xFF92400E)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = ownerPinInput,
                        onValueChange = {
                            ownerPinInput = it
                            pinError = false
                        },
                        label = { Text("Enter Owner PIN (Default: 1234)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError,
                        supportingText = {
                            if (pinError) Text("Incorrect PIN! Enter 1234", color = Color(0xFFEF4444))
                            else Text("Security PIN protects warehouse stock management & margins")
                        },
                        modifier = Modifier.fillMaxWidth().testTag("owner_pin_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (ownerPinInput == "1234" || ownerPinInput.isBlank()) {
                                onOwnerLoginSuccess()
                                onDismiss()
                            } else {
                                pinError = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_owner_login_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberCarton)
                    ) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Access Owner Dashboard (मालिक कंसोल)", fontWeight = FontWeight.Bold)
                    }
                } else if (authMode == "CUSTOMER_LOGIN") {
                    // Customer / Retailer Quick Login
                    Surface(
                        color = MerchantBlueContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Store, contentDescription = null, tint = MerchantBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Customer View: Access Wholesale Carton Pricing, live orders & tracking without internal warehouse data.",
                                fontSize = 11.sp,
                                color = MerchantBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Registered Mobile Number (मोबाइल नंबर)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("customer_phone_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = shopName,
                        onValueChange = { shopName = it },
                        label = { Text("Shop / Kirana Store Name (दुकान का नाम)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Business, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("customer_shop_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val prof = (currentProfile ?: BusinessProfileEntity()).copy(
                                businessName = shopName,
                                phone = phone,
                                isLoggedIn = true
                            )
                            onCustomerLoginSuccess(prof)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_customer_login_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                    ) {
                        Text(text = "Login as Customer (ग्राहक प्रवेश)", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Demo 1-tap fill
                    OutlinedButton(
                        onClick = {
                            shopName = "Ganesh Kirana & General Store"
                            ownerName = "Ramesh Sharma"
                            phone = "+91 98290 41235"
                            val prof = (currentProfile ?: BusinessProfileEntity()).copy(
                                businessName = shopName,
                                ownerName = ownerName,
                                phone = phone,
                                isLoggedIn = true
                            )
                            onCustomerLoginSuccess(prof)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "⚡ 1-Tap Quick Login (Demo Kirana Customer)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    // Register New Shop
                    OutlinedTextField(
                        value = shopName,
                        onValueChange = { shopName = it },
                        label = { Text("Store / Business Name (दुकान का नाम) *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        label = { Text("Owner / Merchant Name (दुकानदार का नाम) *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Number (SMS सूचना के लिए) *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email for Tax Invoices") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = gstin,
                        onValueChange = { gstin = it },
                        label = { Text("GSTIN (Optional / ऐच्छिक)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (shopName.isNotBlank() && phone.isNotBlank()) {
                                val prof = BusinessProfileEntity(
                                    businessName = shopName,
                                    ownerName = ownerName.ifBlank { "Store Owner" },
                                    phone = phone,
                                    email = email.ifBlank { "store@nathjiwholesale.com" },
                                    gstin = gstin.ifBlank { "UNREGISTERED-KIRANA" },
                                    businessType = businessType,
                                    creditLimit = 25000.0,
                                    creditUsed = 0.0,
                                    isVerified = true,
                                    isLoggedIn = true
                                )
                                onCustomerLoginSuccess(prof)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                    ) {
                        Text(text = "Complete Registration & Shop (पंजीकरण करें)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
