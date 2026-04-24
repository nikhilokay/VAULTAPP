package com.myvault.pro.utils

import android.content.Context

object PrefManager {
    private const val PREFS = "vaultpro_prefs"

    private fun p(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isPinSet(ctx: Context) = p(ctx).getBoolean("pin_set", false)
    fun savePin(ctx: Context, pin: String) = p(ctx).edit().putString("pin", pin).putBoolean("pin_set", true).apply()
    fun checkPin(ctx: Context, pin: String) = p(ctx).getString("pin", "") == pin
    fun getPin(ctx: Context) = p(ctx).getString("pin", "") ?: ""

    fun isBiometricEnabled(ctx: Context) = p(ctx).getBoolean("biometric_on", false)
    fun setBiometricEnabled(ctx: Context, on: Boolean) = p(ctx).edit().putBoolean("biometric_on", on).apply()

    fun getAutoLockMinutes(ctx: Context) = p(ctx).getInt("auto_lock_mins", 1)
    fun setAutoLockMinutes(ctx: Context, mins: Int) = p(ctx).edit().putInt("auto_lock_mins", mins).apply()

    fun getLastUnlockTime(ctx: Context) = p(ctx).getLong("last_unlock", 0L)
    fun setLastUnlockTime(ctx: Context) = p(ctx).edit().putLong("last_unlock", System.currentTimeMillis()).apply()

    fun isLocked(ctx: Context): Boolean {
        val lastUnlock = getLastUnlockTime(ctx)
        val autoLockMs = getAutoLockMinutes(ctx) * 60 * 1000L
        return System.currentTimeMillis() - lastUnlock > autoLockMs
    }
}
