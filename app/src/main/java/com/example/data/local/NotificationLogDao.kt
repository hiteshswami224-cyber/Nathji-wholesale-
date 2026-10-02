package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.NotificationLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationLogDao {
    @Query("SELECT * FROM notification_logs ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationLogEntity>>

    @Query("SELECT * FROM notification_logs WHERE orderId = :orderId ORDER BY timestamp DESC")
    fun getNotificationsForOrder(orderId: String): Flow<List<NotificationLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(log: NotificationLogEntity)
}
