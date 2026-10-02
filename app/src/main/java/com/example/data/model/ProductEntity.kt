package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val sku: String,
    val title: String,
    val msrp: Double, // Maximum Retail Price (₹)
    val category: String,
    val subcategory: String,
    val size: String,
    val caption: String,
    val shortDescription: String,
    val longDescription: String,
    val imageUrl: String,
    val currentStock: Int,
    val minStockThreshold: Int = 48,
    val boxSize: Int = 24,           // 1 standard box = 24 packs
    val masterCartonSize: Int = 72,  // 1 master carton = 72 packs (3 boxes)
    val baseWholesaleCost: Double = msrp * 0.84 // Default sample tier price (~16% margin)
)
