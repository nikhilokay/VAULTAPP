package com.myvault.app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myvault.app.data.BankLogin
import com.myvault.app.data.WebLogin
import com.myvault.app.databinding.ActivityAddEditLoginBinding

class AddEditLoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityAddEditLoginBinding
    private val vm: VaultViewModel by viewModels()
    private var editId = 0
    private var type = "web"

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityAddEditLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        type = intent.getStringExtra("type") ?: "web"
        editId = intent.getIntExtra("id", 0)
        val isBank = type == "bank"
        supportActionBar?.title = when { editId!=0&&isBank->"Edit Bank Login"; editId!=0->"Edit Web Login"; isBank->"Add Bank Login"; else->"Add Web Login" }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        b.layoutAccountNumber.visibility = if(isBank) View.VISIBLE else View.GONE
        b.layoutUrl.visibility = if(isBank) View.GONE else View.VISIBLE
        b.layoutTitleLabel.hint = if(isBank) "Bank Name *" else "Site / App Name *"
        if(editId!=0) {
            b.etTitle.setText(if(isBank) intent.getStringExtra("bankName") else intent.getStringExtra("siteName"))
            b.etAccountNumber.setText(intent.getStringExtra("accountNumber"))
            b.etUrl.setText(intent.getStringExtra("url"))
            b.etUsername.setText(intent.getStringExtra("username"))
            b.etPassword.setText(intent.getStringExtra("password"))
            b.etNotes.setText(intent.getStringExtra("notes"))
        }
        b.btnSave.setOnClickListener { save(isBank) }
    }

    private fun save(isBank: Boolean) {
        val title=b.etTitle.text.toString().trim()
        val user=b.etUsername.text.toString().trim()
        val pass=b.etPassword.text.toString().trim()
        val notes=b.etNotes.text.toString().trim()
        if(title.isEmpty()||user.isEmpty()||pass.isEmpty()) { Toast.makeText(this,"Fill required fields",Toast.LENGTH_SHORT).show(); return }
        if(isBank) {
            val acc=b.etAccountNumber.text.toString().trim()
            val l=BankLogin(id=editId, bankName=title, accountNumber=acc, username=user, password=pass, notes=notes)
            if(editId==0) vm.insertBankLogin(l) else vm.updateBankLogin(l)
        } else {
            val url=b.etUrl.text.toString().trim()
            val l=WebLogin(id=editId, siteName=title, url=url, username=user, password=pass, notes=notes)
            if(editId==0) vm.insertWebLogin(l) else vm.updateWebLogin(l)
        }
        Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show(); finish()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
