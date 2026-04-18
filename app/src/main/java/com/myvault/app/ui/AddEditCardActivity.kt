package com.myvault.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myvault.app.data.Card
import com.myvault.app.databinding.ActivityAddEditCardBinding

class AddEditCardActivity : AppCompatActivity() {
    private lateinit var b: ActivityAddEditCardBinding
    private val vm: VaultViewModel by viewModels()
    private var editId = 0

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityAddEditCardBinding.inflate(layoutInflater)
        setContentView(b.root)
        editId = intent.getIntExtra("id", 0)
        supportActionBar?.title = if(editId!=0) "Edit Card" else "Add Card"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(editId!=0) {
            b.etBankName.setText(intent.getStringExtra("bankName"))
            b.etCardholderName.setText(intent.getStringExtra("cardholderName"))
            b.etCardNumber.setText(intent.getStringExtra("cardNumber"))
            b.etExpiry.setText(intent.getStringExtra("expiryDate"))
            b.etCvv.setText(intent.getStringExtra("cvv"))
            b.etCardType.setText(intent.getStringExtra("cardType"))
        }
        b.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name=b.etCardholderName.text.toString().trim()
        val num=b.etCardNumber.text.toString().trim()
        val exp=b.etExpiry.text.toString().trim()
        val cvv=b.etCvv.text.toString().trim()
        val bank=b.etBankName.text.toString().trim()
        val type=b.etCardType.text.toString().trim().ifBlank{"VISA"}
        if(name.isEmpty()||num.isEmpty()||exp.isEmpty()||cvv.isEmpty()||bank.isEmpty()) {
            Toast.makeText(this,"Fill all required fields",Toast.LENGTH_SHORT).show(); return
        }
        val card = Card(id=editId, cardholderName=name, cardNumber=num, expiryDate=exp, cvv=cvv, bankName=bank, cardType=type)
        if(editId==0) vm.insertCard(card) else vm.updateCard(card)
        Toast.makeText(this,"Card saved!",Toast.LENGTH_SHORT).show(); finish()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
