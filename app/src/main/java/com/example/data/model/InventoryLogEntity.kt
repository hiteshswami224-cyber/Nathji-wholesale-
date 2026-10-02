package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_logs")
data class InventoryLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sku: String,
    val productTitle: String,
    val changeAmount: Int,          // e.g. +72, -24
    val resultingStock: Int,
    val actionType: String,         // "ORDER_DISPATCH", "RESTOCK", "MANUAL_ADJUSTMENT", "INITIAL_RECEIPT"
    val notes: String,
    val timestamp: Long = System.currentTimeMillis()
)
