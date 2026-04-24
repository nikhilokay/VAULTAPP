package com.myvault.pro.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.myvault.pro.R
import com.myvault.pro.utils.PrefManager

class LockActivity : AppCompatActivity() {

    private val pin = StringBuilder()
    private var isSettingPin = false

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var dot1: View; private lateinit var dot2: View
    private lateinit var dot3: View; private lateinit var dot4: View
    private lateinit var btnBiometric: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        isSettingPin = !PrefManager.isPinSet(this)

        tvTitle = findViewById(R.id.tvTitle)
        tvSubtitle = findViewById(R.id.tvSubtitle)
        dot1 = findViewById(R.id.dot1); dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3); dot4 = findViewById(R.id.dot4)
        btnBiometric = findViewById(R.id.btnBiometric)

        tvTitle.text = if (isSettingPin) "Set Your PIN" else "Welcome Back!"
        tvSubtitle.text = if (isSettingPin) "Choose a 4-digit PIN to protect your vault" else "Enter PIN or use biometric"

        // Number buttons
        for (i in 0..9) {
            val id = resources.getIdentifier("btn$i", "id", packageName)
            findViewById<Button>(id).setOnClickListener { addDigit(i.toString()) }
        }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (pin.isNotEmpty()) { pin.deleteCharAt(pin.length - 1); updateDots() }
        }
        findViewById<Button>(R.id.btnOk).setOnClickListener { submitPin() }

        // Biometric button
        if (!isSettingPin && PrefManager.isBiometricEnabled(this) && canUseBiometric()) {
            btnBiometric.visibility = View.VISIBLE
            btnBiometric.setOnClickListener { showBiometricPrompt() }
            showBiometricPrompt() // auto-show on open
        } else {
            btnBiometric.visibility = View.GONE
        }
    }

    private fun canUseBiometric(): Boolean {
        val bm = BiometricManager.from(this)
        return bm.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                openMain()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Just fall back to PIN - no need to show error
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@LockActivity, "Biometric failed, use PIN", Toast.LENGTH_SHORT).show()
            }
        }

        val prompt = BiometricPrompt(this, executor, callback)
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock My Vault")
            .setSubtitle("Use fingerprint or face to unlock")
            .setNegativeButtonText("Use PIN")
            .build()
        prompt.authenticate(info)
    }

    private fun addDigit(d: String) {
        if (pin.length < 4) {
            pin.append(d); updateDots()
            if (pin.length == 4) submitPin()
        }
    }

    private fun updateDots() {
        dot1.alpha = if (pin.length >= 1) 1f else 0.25f
        dot2.alpha = if (pin.length >= 2) 1f else 0.25f
        dot3.alpha = if (pin.length >= 3) 1f else 0.25f
        dot4.alpha = if (pin.length >= 4) 1f else 0.25f
    }

    private fun submitPin() {
        val entered = pin.toString()
        if (isSettingPin) {
            PrefManager.savePin(this, entered)
            Toast.makeText(this, "PIN set! Vault is ready 🔐", Toast.LENGTH_SHORT).show()
            openMain()
        } else {
            if (PrefManager.checkPin(this, entered)) {
                openMain()
            } else {
                Toast.makeText(this, "Wrong PIN! Try again.", Toast.LENGTH_SHORT).show()
                pin.clear(); updateDots()
            }
        }
    }

    private fun openMain() {
        PrefManager.setLastUnlockTime(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
