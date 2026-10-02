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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Verified
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
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.NotificationLogEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.SavedPaymentMethodEntity
import com.example.data.model.WholesalePricingHelper
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
fun AccountScreen(
    profile: BusinessProfileEntity?,
    addresses: List<SavedAddressEntity>,
    paymentMethods: List<SavedPaymentMethodEntity>,
    notifications: List<NotificationLogEntity>,
    onEditProfileClick: () -> Unit,
    onAddAddressClick: () -> Unit,
    onSetDefaultAddress: (Long) -> Unit,
    onDeleteAddress: (Long) -> Unit,
    onSetDefaultPayment: (Long) -> Unit,
    onOpenAuthDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bProfile = profile ?: BusinessProfileEntity()

    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Customer Auth / Switch Account Shortcut Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MerchantBlueContainer),
                border = BorderStroke(1.dp, MerchantBlue.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Customer Account / Role Access", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MerchantBlue)
                        Text(text = "Logged in as ${bProfile.businessName}. Tap to switch customer or access Owner Admin.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Button(
                        onClick = onOpenAuthDialog,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                        modifier = Modifier.height(34.dp).testTag("account_auth_dialog_btn")
                    ) {
                        Text(text = "Login / Switch", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Business Profile Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("business_profile_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MerchantBlueContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = null,
                                    tint = MerchantBlue,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = bProfile.businessName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    if (bProfile.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "Verified Buyer",
                                            tint = MerchantBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${bProfile.ownerName} • ${bProfile.businessType}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onEditProfileClick,
                            modifier = Modifier.testTag("edit_profile_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile", tint = MerchantBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = BorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Contact & Tax Info
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "GSTIN / TAX ID", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = bProfile.gstin, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "TRADE LICENSE", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = bProfile.tradeLicense, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = MerchantBlue)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = bProfile.phone, fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(14.dp), tint = AmberCarton)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = bProfile.email, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // B2B Wholesale Khata / Credit Line Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MerchantBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "15-Day B2B Khata Credit Line", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(AmberContainer).padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "0% INTEREST", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val availableCredit = (bProfile.creditLimit - bProfile.creditUsed).coerceAtLeast(0.0)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "APPROVED LIMIT", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = WholesalePricingHelper.formatCurrency(bProfile.creditLimit), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(text = "USED CREDIT", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = WholesalePricingHelper.formatCurrency(bProfile.creditUsed), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "AVAILABLE FOR CARTONS", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = WholesalePricingHelper.formatCurrency(availableCredit), color = Color(0xFF6EE7B7), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pay within 15 days of delivery. Instant verification for registered shops.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Saved Shipping Addresses Header & List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Saved Shipping Addresses", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Deliver cartons directly to shopfront or godown", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    onClick = onAddAddressClick,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue),
                    modifier = Modifier.height(34.dp).testTag("add_address_btn"),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add Address", fontSize = 11.sp)
                }
            }
        }

        items(addresses, key = { it.id }) { address ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, if (address.isDefault) MerchantBlue else BorderLight)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = MerchantBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = address.label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            if (address.isDefault) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(AmberContainer).padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "DEFAULT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AmberCarton)
                                }
                            }
                        }

                        Row {
                            if (!address.isDefault) {
                                OutlinedButton(
                                    onClick = { onSetDefaultAddress(address.id) },
                                    modifier = Modifier.height(28.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp)
                                ) {
                                    Text(text = "Set Default", fontSize = 10.sp)
                                }
                            }
                            IconButton(onClick = { onDeleteAddress(address.id) }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = address.streetAddress, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "${address.city}, ${address.state} - ${address.pincode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Contact: ${address.contactPerson} (${address.phone})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Saved Payment Methods
        item {
            Column {
                Text(text = "Saved Wholesale Payment Methods", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Preferred payment options for instant dispatch", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        items(paymentMethods, key = { it.id }) { method ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Payment, contentDescription = null, tint = MerchantBlue, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = method.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                if (method.isDefault) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(AmberContainer).padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(text = "DEFAULT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = AmberCarton)
                                    }
                                }
                            }
                            Text(text = method.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    if (!method.isDefault) {
                        OutlinedButton(
                            onClick = { onSetDefaultPayment(method.id) },
                            modifier = Modifier.height(28.dp),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp)
                        ) {
                            Text(text = "Set Default", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Notification Log History (SMS & Email audit)
        item {
            Column {
                Text(text = "Automated Order Notification History (${notifications.size})", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "SMS and Email receipts dispatched to your business contacts", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        items(notifications.take(8), key = { it.id }) { notif ->
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (notif.channel == "SMS") Icons.Default.Sms else Icons.Default.Email,
                                contentDescription = notif.channel,
                                tint = if (notif.channel == "SMS") MerchantBlue else AmberCarton,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${notif.channel} • ${notif.subject}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Text(text = sdf.format(Date(notif.timestamp)), fontSize = 10.sp, color = Color(0xFF94A3B8))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = notif.messageContent, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Sent to: ${notif.recipient} (${notif.deliveryStatus})", fontSize = 10.sp, color = EmeraldProfitDark, fontWeight = FontWeight.Medium)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
