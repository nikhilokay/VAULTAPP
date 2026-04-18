package com.myvault.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myvault.app.databinding.ActivityPinBinding
import com.myvault.app.utils.PinManager

class PinActivity : AppCompatActivity() {
    private lateinit var b: ActivityPinBinding
    private val pin = StringBuilder()
    private var setting = false

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityPinBinding.inflate(layoutInflater)
        setContentView(b.root)
        setting = !PinManager.isPinSet(this)
        b.tvTitle.text = if (setting) "Set Your Master PIN" else "Enter PIN to Unlock"
        b.tvSubtitle.text = if (setting) "Choose a 4-digit PIN" else "Vault is locked 🔒"
        setupKeypad()
    }

    private fun setupKeypad() {
        listOf(b.btn0,b.btn1,b.btn2,b.btn3,b.btn4,b.btn5,b.btn6,b.btn7,b.btn8,b.btn9)
            .forEachIndexed { i, btn -> btn.setOnClickListener { addDigit(i.toString()) } }
        b.btnDelete.setOnClickListener { if (pin.isNotEmpty()) { pin.deleteCharAt(pin.length-1); updateDots() } }
        b.btnOk.setOnClickListener { submit() }
    }

    private fun addDigit(d: String) {
        if (pin.length < 4) { pin.append(d); updateDots(); if (pin.length == 4) submit() }
    }

    private fun updateDots() {
        listOf(b.dot1,b.dot2,b.dot3,b.dot4).forEachIndexed { i, dot ->
            dot.alpha = if (i < pin.length) 1f else 0.3f
        }
    }

    private fun submit() {
        if (pin.length < 4) { Toast.makeText(this,"Enter 4 digits",Toast.LENGTH_SHORT).show(); return }
        if (setting) {
            PinManager.save(this, pin.toString())
            startActivity(Intent(this, MainActivity::class.java)); finish()
        } else {
            if (PinManager.check(this, pin.toString())) {
                startActivity(Intent(this, MainActivity::class.java)); finish()
            } else {
                Toast.makeText(this,"Wrong PIN!",Toast.LENGTH_SHORT).show()
                pin.clear(); updateDots()
            }
        }
    }
}
