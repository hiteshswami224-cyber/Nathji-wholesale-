package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalAmount: Double,        // Net wholesale bill
    val totalRetailValue: Double,   // Total value at MSRP
    val totalRetailerProfit: Double,// Profit shopkeeper will earn
    val totalItemsCount: Int,       // Total units
    val status: String,             // "Processing", "Packed & Staged", "Dispatched", "In Transit", "Out for Delivery", "Delivered"
    val paymentMethod: String,      // "Pay on Delivery (Cash/UPI)", "15-Day Khata Credit", "Immediate NetBanking"
    val retailerName: String,
    val retailerGstin: String,
    val retailerAddress: String,
    val recipientPhone: String = "+91 98290 41235",
    val recipientEmail: String = "store@sharmakirana.com",
    val carrierName: String = "Delhivery B2B Logistics",
    val carrierTrackingNumber: String = "DLV-B2B-9842103",
    val estimatedDeliveryDate: String = "Tomorrow, by 4:00 PM",
    val carrierStatusDetails: String = "Departed Jaipur Sorting Facility, en route to destination hub",
    val carrierPhone: String = "+91 1800 102 3456",
    val carrierTrackingUrl: String = "https://www.delhivery.com",
    val smsNotificationSent: Boolean = true,
    val emailNotificationSent: Boolean = true,
    val itemsSummary: String        // Formatted summary of items
)

data class TrackingMilestone(
    val stage: String,
    val title: String,
    val description: String,
    val timestampText: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)
