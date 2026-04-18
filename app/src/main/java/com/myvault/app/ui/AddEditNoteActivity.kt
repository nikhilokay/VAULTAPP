package com.myvault.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myvault.app.data.SecureNote
import com.myvault.app.databinding.ActivityAddEditNoteBinding

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var b: ActivityAddEditNoteBinding
    private val vm: VaultViewModel by viewModels()
    private var editId = 0

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(b.root)
        editId = intent.getIntExtra("id", 0)
        supportActionBar?.title = if(editId!=0) "Edit Note" else "Add Secure Note"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(editId!=0) {
            b.etTitle.setText(intent.getStringExtra("title"))
            b.etContent.setText(intent.getStringExtra("content"))
        }
        b.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val title = b.etTitle.text.toString().trim()
        val content = b.etContent.text.toString().trim()
        if(title.isEmpty()||content.isEmpty()) { Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show(); return }
        val note = SecureNote(id=editId, title=title, content=content)
        if(editId==0) vm.insertNote(note) else vm.updateNote(note)
        Toast.makeText(this,"Note saved!",Toast.LENGTH_SHORT).show(); finish()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
