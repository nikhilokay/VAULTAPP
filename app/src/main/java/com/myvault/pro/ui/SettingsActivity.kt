package com.myvault.pro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import com.myvault.pro.R
import com.myvault.pro.utils.BackupManager
import com.myvault.pro.utils.PrefManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_settings)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        // Biometric toggle
        val switchBio = findViewById<Switch>(R.id.switchBiometric)
        val canUseBio = canUseBiometric()
        switchBio.isEnabled = canUseBio
        switchBio.isChecked = PrefManager.isBiometricEnabled(this)
        if (!canUseBio) {
            findViewById<TextView>(R.id.tvBiometricDesc).text = "No fingerprint/face registered on this device"
        }
        switchBio.setOnCheckedChangeListener { _, on ->
            PrefManager.setBiometricEnabled(this, on)
            Toast.makeText(this, if (on) "Biometric enabled" else "Biometric disabled", Toast.LENGTH_SHORT).show()
        }

        // Auto lock
        val spinnerLock = findViewById<Spinner>(R.id.spinnerAutoLock)
        val lockOptions = listOf("30 seconds", "1 minute", "5 minutes", "15 minutes", "Never")
        val lockMins = listOf(0, 1, 5, 15, 9999)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lockOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLock.adapter = adapter
        val currentMins = PrefManager.getAutoLockMinutes(this)
        spinnerLock.setSelection(lockMins.indexOf(currentMins).coerceAtLeast(0))
        spinnerLock.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, pos: Int, id: Long) {
                PrefManager.setAutoLockMinutes(this@SettingsActivity, lockMins[pos])
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        // Change PIN
        findViewById<LinearLayout>(R.id.layoutChangePin).setOnClickListener {
            startActivity(Intent(this, ChangePinActivity::class.java))
        }

        // Backup
        findViewById<LinearLayout>(R.id.layoutBackup).setOnClickListener {
            val result = BackupManager.backup(this)
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        }

        // Restore
        findViewById<LinearLayout>(R.id.layoutRestore).setOnClickListener {
            Toast.makeText(this, "Put backup file path in the field below and tap Restore", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.btnRestore).setOnClickListener {
            val path = findViewById<EditText>(R.id.etRestorePath).text.toString().trim()
            if (path.isEmpty()) {
                Toast.makeText(this, "Enter the backup file path", Toast.LENGTH_SHORT).show()
            } else {
                val result = BackupManager.restore(this, path)
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun canUseBiometric(): Boolean {
        val bm = BiometricManager.from(this)
        return bm.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }
}
