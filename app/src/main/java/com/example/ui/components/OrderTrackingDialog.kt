package com.example.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sms
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NotificationLogEntity
import com.example.data.model.OrderEntity
import com.example.data.model.TrackingMilestone
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
fun OrderTrackingDialog(
    order: OrderEntity,
    milestones: List<TrackingMilestone>,
    notifications: List<NotificationLogEntity>,
    onDismiss: () -> Unit,
    onAdvanceStatus: (orderId: String, newStatus: String, locationNote: String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val orderNotifications = remember(notifications, order.orderId) {
        notifications.filter { it.orderId == order.orderId }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .testTag("order_tracking_dialog"),
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
                // Top bar with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Live Order Tracking",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CARRIER SYNCED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldProfitDark
                                )
                            }
                        }
                        Text(
                            text = "Order ID: ${order.orderId}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_tracking_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Hero Status Card
                Surface(
                    color = MerchantBlue,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = order.status.uppercase(Locale.US),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Estimated Delivery: ${order.estimatedDeliveryDate}",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Payment mode pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AmberCarton)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = order.paymentMethod,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Current Carrier Location / Status Note
                        Text(
                            text = "CURRENT CARRIER LOCATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = order.carrierStatusDetails,
                            fontSize = 13.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Integrated Shipping Carrier Details
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Logistics Carrier Partner",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = order.carrierName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MerchantBlueContainer)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "B2B SURFACE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MerchantBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // AWB & Tracking Number row with copy button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE2E8F0).copy(alpha = 0.5f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Air Waybill (AWB) / Tracking No.",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = order.carrierTrackingNumber,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(order.carrierTrackingNumber))
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("copy_awb_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy AWB",
                                        tint = MerchantBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(order.carrierTrackingUrl))
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Handle fallback
                                        }
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("open_carrier_url_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInNew,
                                        contentDescription = "Open Web Tracker",
                                        tint = MerchantBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Carrier Contact Support
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Carrier Helpline: ${order.carrierPhone}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.carrierPhone}"))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {}
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(30.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Call", fontSize = 10.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Multi-Milestone Tracking Timeline
                Text(
                    text = "Delivery Milestones",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Real-time scans from warehouse and carrier distribution hubs",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Timeline List
                milestones.forEachIndexed { index, milestone ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Timeline Indicator dot / checkmark + vertical connecting line
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(28.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            milestone.isCompleted -> EmeraldProfit
                                            milestone.isCurrent -> MerchantBlue
                                            else -> Color(0xFFCBD5E1)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (milestone.isCompleted && !milestone.isCurrent) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else if (milestone.isCurrent) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                            }

                            if (index < milestones.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(34.dp)
                                        .background(
                                            if (milestone.isCompleted) EmeraldProfit else Color(0xFFE2E8F0)
                                        )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Milestone text content
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = milestone.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (milestone.isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (milestone.isCurrent) MerchantBlue else if (milestone.isCompleted) MaterialTheme.colorScheme.onSurface else Color(0xFF94A3B8)
                                )
                                Text(
                                    text = milestone.timestampText,
                                    fontSize = 10.sp,
                                    color = if (milestone.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF94A3B8)
                                )
                            }

                            Text(
                                text = milestone.description,
                                fontSize = 11.sp,
                                color = if (milestone.isCurrent) MaterialTheme.colorScheme.onSurface else Color(0xFF64748B),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = BorderLight)
                Spacer(modifier = Modifier.height(14.dp))

                // Automated SMS & Email Notification History
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Automated Dispatch Alerts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AmberContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SMS & EMAIL ACTIVE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberCarton
                                )
                            }
                        }
                        Text(
                            text = "Automated notifications sent to ${order.recipientPhone} & ${order.recipientEmail}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (orderNotifications.isEmpty()) {
                    Text(
                        text = "No notification dispatches recorded yet.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    orderNotifications.forEach { notif ->
                        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
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
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${notif.channel} • ${notif.subject}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Text(
                                        text = sdf.format(Date(notif.timestamp)),
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.messageContent,
                                    fontSize = 11.sp,
                                    color = Color(0xFF334155),
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Status: ${notif.deliveryStatus} to ${notif.recipient}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldProfitDark
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = BorderLight)
                Spacer(modifier = Modifier.height(12.dp))

                // Carrier Checkpoint Simulation & Testing Controls
                Text(
                    text = "Carrier Scan Simulation (Store Manager / Demo)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap to simulate the carrier scanning the parcel to test live status progression and automated SMS/Email alert dispatch",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (order.status == "Processing") {
                        Button(
                            onClick = {
                                onAdvanceStatus(
                                    order.orderId,
                                    "Dispatched",
                                    "Handed over to ${order.carrierName} hub. E-Way bill validated."
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("sim_dispatch_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                        ) {
                            Text(text = "Simulate: Dispatched", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (order.status == "Dispatched" || order.status == "Packed & Staged") {
                        Button(
                            onClick = {
                                onAdvanceStatus(
                                    order.orderId,
                                    "In Transit",
                                    "Departed Jaipur Sorting Hub, en route to destination local delivery center"
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("sim_transit_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MerchantBlue)
                        ) {
                            Text(text = "Simulate: In Transit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (order.status == "In Transit") {
                        Button(
                            onClick = {
                                onAdvanceStatus(
                                    order.orderId,
                                    "Out for Delivery",
                                    "Vehicle assigned. Driver loaded cartons for shop delivery."
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("sim_out_delivery_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberCarton)
                        ) {
                            Text(text = "Simulate: Out for Delivery", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (order.status == "Out for Delivery") {
                        Button(
                            onClick = {
                                onAdvanceStatus(
                                    order.orderId,
                                    "Delivered",
                                    "Delivered to ${order.retailerName}. Verified by store manager."
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("sim_delivered_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldProfit)
                        ) {
                            Text(text = "Simulate: Delivered", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓ Order Delivered & Completed",
                                fontWeight = FontWeight.Bold,
                                color = EmeraldProfitDark,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
