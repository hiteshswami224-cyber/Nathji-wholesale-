package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY title ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE sku = :sku")
    fun getProductBySku(sku: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE sku = :sku")
    suspend fun getProductDirect(sku: String): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE sku = :sku")
    suspend fun deleteProductBySku(sku: String)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET currentStock = :newStock WHERE sku = :sku")
    suspend fun updateStock(sku: String, newStock: Int)

    @Query("UPDATE products SET currentStock = MAX(0, currentStock + :delta) WHERE sku = :sku")
    suspend fun adjustStock(sku: String, delta: Int)
}
