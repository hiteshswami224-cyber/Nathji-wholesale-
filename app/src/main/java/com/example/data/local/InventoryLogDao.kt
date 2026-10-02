package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.InventoryLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryLogDao {
    @Query("SELECT * FROM inventory_logs ORDER BY timestamp DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<InventoryLogEntity>>

    @Query("SELECT * FROM inventory_logs WHERE sku = :sku ORDER BY timestamp DESC")
    fun getLogsForSku(sku: String): Flow<List<InventoryLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: InventoryLogEntity)
}
