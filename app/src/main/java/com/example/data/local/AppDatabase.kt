package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.InventoryLogEntity
import com.example.data.model.NotificationLogEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.SavedPaymentMethodEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        InventoryLogEntity::class,
        NotificationLogEntity::class,
        BusinessProfileEntity::class,
        SavedAddressEntity::class,
        SavedPaymentMethodEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun inventoryLogDao(): InventoryLogDao
    abstract fun notificationLogDao(): NotificationLogDao
    abstract fun businessProfileDao(): BusinessProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nathji_wholesale.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
