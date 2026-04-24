package com.myvault.pro.data

import androidx.room.*

@Entity(tableName = "vault_items")
data class VaultItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,       // CARD, BANK, WEB, NOTE, WIFI, ID, CUSTOM
    val title: String,
    val subtitle: String = "",  // card number / account / username / ssid
    val field1: String = "",    // cardholder / password / password / password
    val field2: String = "",    // expiry / branch / url / notes
    val field3: String = "",    // cvv / ifsc / notes / -
    val field4: String = "",    // card type / account no / - / -
    val notes: String = "",
    val color: String = "BLUE", // BLUE, GREEN, AMBER, PURPLE, RED
    val createdAt: Long = System.currentTimeMillis()
)

object Categories {
    const val CARD = "CARD"
    const val BANK = "BANK"
    const val WEB = "WEB"
    const val NOTE = "NOTE"
    const val WIFI = "WIFI"
    const val ID = "ID"
    const val CUSTOM = "CUSTOM"

    val all = listOf(CARD, BANK, WEB, NOTE, WIFI, ID, CUSTOM)

    fun icon(cat: String) = when(cat) {
        CARD -> "💳"
        BANK -> "🏦"
        WEB -> "🌐"
        NOTE -> "📝"
        WIFI -> "📶"
        ID -> "🪪"
        CUSTOM -> "📁"
        else -> "📁"
    }

    fun color(cat: String) = when(cat) {
        CARD -> "#4F8EF7"
        BANK -> "#22C55E"
        WEB -> "#F59E0B"
        NOTE -> "#A855F7"
        WIFI -> "#06B6D4"
        ID -> "#EF4444"
        CUSTOM -> "#8B5CF6"
        else -> "#4F8EF7"
    }
}
