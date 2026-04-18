package com.myvault.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao interface CardDao {
    @Query("SELECT * FROM cards ORDER BY bankName ASC") fun getAll(): LiveData<List<Card>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(c: Card)
    @Update suspend fun update(c: Card)
    @Delete suspend fun delete(c: Card)
}

@Dao interface BankLoginDao {
    @Query("SELECT * FROM bank_logins ORDER BY bankName ASC") fun getAll(): LiveData<List<BankLogin>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(l: BankLogin)
    @Update suspend fun update(l: BankLogin)
    @Delete suspend fun delete(l: BankLogin)
}

@Dao interface WebLoginDao {
    @Query("SELECT * FROM web_logins ORDER BY siteName ASC") fun getAll(): LiveData<List<WebLogin>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(l: WebLogin)
    @Update suspend fun update(l: WebLogin)
    @Delete suspend fun delete(l: WebLogin)
}

@Dao interface SecureNoteDao {
    @Query("SELECT * FROM secure_notes ORDER BY createdAt DESC") fun getAll(): LiveData<List<SecureNote>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(n: SecureNote)
    @Update suspend fun update(n: SecureNote)
    @Delete suspend fun delete(n: SecureNote)
}
