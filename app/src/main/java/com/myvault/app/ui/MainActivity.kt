package com.myvault.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.myvault.app.R
import com.myvault.app.databinding.ActivityMainBinding
import com.myvault.app.ui.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        supportActionBar?.title = "🔐 My Vault"
        load(CardsFragment())

        b.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_cards -> { load(CardsFragment()); true }
                R.id.nav_bank  -> { load(BankLoginsFragment()); true }
                R.id.nav_web   -> { load(WebLoginsFragment()); true }
                R.id.nav_notes -> { load(NotesFragment()); true }
                else -> false
            }
        }

        b.fab.setOnClickListener {
            val intent = when(b.bottomNav.selectedItemId) {
                R.id.nav_cards -> Intent(this, AddEditCardActivity::class.java)
                R.id.nav_bank  -> Intent(this, AddEditLoginActivity::class.java).apply { putExtra("type","bank") }
                R.id.nav_web   -> Intent(this, AddEditLoginActivity::class.java).apply { putExtra("type","web") }
                R.id.nav_notes -> Intent(this, AddEditNoteActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }
    }

    private fun load(f: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit()
}
