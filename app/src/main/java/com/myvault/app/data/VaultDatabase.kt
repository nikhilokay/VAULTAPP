package com.myvault.app.data

import android.content.Context
import androidx.room.*

@Database(entities = [Card::class, BankLogin::class, WebLogin::class, SecureNote::class], version = 1, exportSchema = false)
abstract class VaultDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun bankLoginDao(): BankLoginDao
    abstract fun webLoginDao(): WebLoginDao
    abstract fun secureNoteDao(): SecureNoteDao

    companion object {
        @Volatile private var INSTANCE: VaultDatabase? = null
        fun get(context: Context) = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, VaultDatabase::class.java, "vault_db")
                .fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}
