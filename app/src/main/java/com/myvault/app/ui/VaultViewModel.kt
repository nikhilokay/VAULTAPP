package com.myvault.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myvault.app.data.*
import kotlinx.coroutines.launch

class VaultViewModel(app: Application) : AndroidViewModel(app) {
    private val db = VaultDatabase.get(app)

    val allCards = db.cardDao().getAll()
    fun insertCard(c: Card) = viewModelScope.launch { db.cardDao().insert(c) }
    fun updateCard(c: Card) = viewModelScope.launch { db.cardDao().update(c) }
    fun deleteCard(c: Card) = viewModelScope.launch { db.cardDao().delete(c) }

    val allBankLogins = db.bankLoginDao().getAll()
    fun insertBankLogin(l: BankLogin) = viewModelScope.launch { db.bankLoginDao().insert(l) }
    fun updateBankLogin(l: BankLogin) = viewModelScope.launch { db.bankLoginDao().update(l) }
    fun deleteBankLogin(l: BankLogin) = viewModelScope.launch { db.bankLoginDao().delete(l) }

    val allWebLogins = db.webLoginDao().getAll()
    fun insertWebLogin(l: WebLogin) = viewModelScope.launch { db.webLoginDao().insert(l) }
    fun updateWebLogin(l: WebLogin) = viewModelScope.launch { db.webLoginDao().update(l) }
    fun deleteWebLogin(l: WebLogin) = viewModelScope.launch { db.webLoginDao().delete(l) }

    val allNotes = db.secureNoteDao().getAll()
    fun insertNote(n: SecureNote) = viewModelScope.launch { db.secureNoteDao().insert(n) }
    fun updateNote(n: SecureNote) = viewModelScope.launch { db.secureNoteDao().update(n) }
    fun deleteNote(n: SecureNote) = viewModelScope.launch { db.secureNoteDao().delete(n) }
}
