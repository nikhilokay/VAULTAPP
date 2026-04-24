package com.myvault.pro.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VaultDao {
    @Query("SELECT * FROM vault_items ORDER BY createdAt DESC")
    fun getAll(): LiveData<List<VaultItem>>

    @Query("SELECT * FROM vault_items WHERE category = :cat ORDER BY createdAt DESC")
    fun getByCategory(cat: String): LiveData<List<VaultItem>>

    @Query("SELECT * FROM vault_items WHERE title LIKE '%' || :q || '%' OR subtitle LIKE '%' || :q || '%' ORDER BY createdAt DESC")
    fun search(q: String): LiveData<List<VaultItem>>

    @Query("SELECT * FROM vault_items ORDER BY createdAt DESC")
    fun getAllSync(): List<VaultItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: VaultItem): Long

    @Update
    suspend fun update(item: VaultItem)

    @Delete
    suspend fun delete(item: VaultItem)

    @Query("SELECT COUNT(*) FROM vault_items WHERE category = :cat")
    fun countByCategory(cat: String): LiveData<Int>
}
