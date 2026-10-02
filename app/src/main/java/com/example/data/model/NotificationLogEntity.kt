package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_logs")
data class NotificationLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,
    val channel: String, // "SMS", "EMAIL", "WHATSAPP"
    val recipient: String,
    val subject: String,
    val messageContent: String,
    val timestamp: Long = System.currentTimeMillis(),
    val deliveryStatus: String = "Delivered" // "Delivered", "Sent"
)
