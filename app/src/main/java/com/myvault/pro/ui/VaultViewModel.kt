package com.myvault.pro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myvault.pro.data.VaultDatabase
import com.myvault.pro.data.VaultItem
import kotlinx.coroutines.launch

class VaultViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = VaultDatabase.get(app).vaultDao()

    val allItems = dao.getAll()
    val currentCategory = MutableLiveData("ALL")

    fun getByCategory(cat: String) = dao.getByCategory(cat)
    fun search(q: String) = dao.search(q)

    fun insert(item: VaultItem) = viewModelScope.launch { dao.insert(item) }
    fun update(item: VaultItem) = viewModelScope.launch { dao.update(item) }
    fun delete(item: VaultItem) = viewModelScope.launch { dao.delete(item) }

    fun countByCategory(cat: String) = dao.countByCategory(cat)
}
