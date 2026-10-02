package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val sku: String,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)
