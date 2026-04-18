package com.myvault.app.data

import androidx.room.*

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardholderName: String, val cardNumber: String,
    val expiryDate: String, val cvv: String,
    val bankName: String, val cardType: String
)

@Entity(tableName = "bank_logins")
data class BankLogin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bankName: String, val accountNumber: String,
    val username: String, val password: String, val notes: String = ""
)

@Entity(tableName = "web_logins")
data class WebLogin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val siteName: String, val url: String,
    val username: String, val password: String, val notes: String = ""
)

@Entity(tableName = "secure_notes")
data class SecureNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
