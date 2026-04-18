package com.myvault.app.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object PinManager {
    private const val PREFS = "vault_prefs"
    private const val KEY_PIN = "pin"
    private const val KEY_SET = "pin_set"

    private fun prefs(ctx: Context) = try {
        val key = MasterKey.Builder(ctx).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(ctx, PREFS, key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    } catch (e: Exception) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    fun isPinSet(ctx: Context) = prefs(ctx).getBoolean(KEY_SET, false)
    fun save(ctx: Context, pin: String) = prefs(ctx).edit().putString(KEY_PIN, pin).putBoolean(KEY_SET, true).apply()
    fun check(ctx: Context, pin: String) = prefs(ctx).getString(KEY_PIN, "") == pin
}
