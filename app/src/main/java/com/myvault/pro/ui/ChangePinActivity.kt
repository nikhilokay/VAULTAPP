package com.myvault.pro.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.myvault.pro.R
import com.myvault.pro.utils.PrefManager

class ChangePinActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_change_pin)
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnChangePin).setOnClickListener {
            val current = findViewById<EditText>(R.id.etCurrentPin).text.toString()
            val newPin = findViewById<EditText>(R.id.etNewPin).text.toString()
            val confirm = findViewById<EditText>(R.id.etConfirmPin).text.toString()
            when {
                !PrefManager.checkPin(this, current) -> Toast.makeText(this, "Current PIN is wrong!", Toast.LENGTH_SHORT).show()
                newPin.length != 4 -> Toast.makeText(this, "New PIN must be 4 digits", Toast.LENGTH_SHORT).show()
                newPin != confirm -> Toast.makeText(this, "PINs don't match!", Toast.LENGTH_SHORT).show()
                else -> {
                    PrefManager.savePin(this, newPin)
                    Toast.makeText(this, "PIN changed successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
