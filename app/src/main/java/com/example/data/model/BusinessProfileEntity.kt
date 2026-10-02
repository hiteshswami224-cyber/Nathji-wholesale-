package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_profiles")
data class BusinessProfileEntity(
    @PrimaryKey
    val id: String = "PRIMARY_PROFILE",
    val businessName: String = "Ganesh Kirana & General Store",
    val ownerName: String = "Ramesh Sharma",
    val email: String = "sharma.kirana@gmail.com",
    val phone: String = "+91 98290 41235",
    val gstin: String = "08AABCG1234F1Z8",
    val businessType: String = "Retail Kirana & FMCG Store",
    val tradeLicense: String = "RJ-JPR-2024-88412",
    val creditLimit: Double = 50000.0,
    val creditUsed: Double = 12450.0,
    val creditPaymentDays: Int = 15,
    val isVerified: Boolean = true,
    val isLoggedIn: Boolean = true
)

@Entity(tableName = "saved_addresses")
data class SavedAddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String,          // e.g. "Main Storefront", "Warehouse Depot #1"
    val contactPerson: String,
    val phone: String,
    val streetAddress: String,
    val city: String,
    val state: String,
    val pincode: String,
    val isDefault: Boolean = false
)

@Entity(tableName = "saved_payment_methods")
data class SavedPaymentMethodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,           // "KHATA_CREDIT", "UPI", "NETBANKING", "COD"
    val title: String,          // "15-Day Khata Credit Line", "HDFC Current A/C (UPI)"
    val subtitle: String,       // "₹37,550 Available • 0% Interest", "upi@hdfcbank"
    val isDefault: Boolean = false
)
