package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE sku = :sku")
    suspend fun getCartItem(sku: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE sku = :sku")
    suspend fun updateQuantity(sku: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE sku = :sku")
    suspend fun deleteItem(sku: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
