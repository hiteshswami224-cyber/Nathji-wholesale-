package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.SavedPaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessProfileDao {
    @Query("SELECT * FROM business_profiles WHERE id = 'PRIMARY_PROFILE' LIMIT 1")
    fun getProfile(): Flow<BusinessProfileEntity?>

    @Query("SELECT * FROM business_profiles WHERE id = 'PRIMARY_PROFILE' LIMIT 1")
    suspend fun getProfileDirect(): BusinessProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: BusinessProfileEntity)

    // Saved Addresses
    @Query("SELECT * FROM saved_addresses ORDER BY isDefault DESC, id ASC")
    fun getAllAddresses(): Flow<List<SavedAddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: SavedAddressEntity)

    @Query("UPDATE saved_addresses SET isDefault = 0")
    suspend fun clearDefaultAddress()

    @Query("UPDATE saved_addresses SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultAddress(id: Long)

    @Query("DELETE FROM saved_addresses WHERE id = :id")
    suspend fun deleteAddress(id: Long)

    // Saved Payment Methods
    @Query("SELECT * FROM saved_payment_methods ORDER BY isDefault DESC, id ASC")
    fun getAllPaymentMethods(): Flow<List<SavedPaymentMethodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(method: SavedPaymentMethodEntity)

    @Query("UPDATE saved_payment_methods SET isDefault = 0")
    suspend fun clearDefaultPaymentMethod()

    @Query("UPDATE saved_payment_methods SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultPaymentMethod(id: Long)
}
