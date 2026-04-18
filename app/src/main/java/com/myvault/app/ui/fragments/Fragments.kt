package com.myvault.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.myvault.app.databinding.*
import com.myvault.app.ui.*
import com.myvault.app.ui.adapters.*

class CardsFragment : Fragment() {
    private var _b: FragmentCardsBinding? = null
    private val b get() = _b!!
    private val vm: VaultViewModel by activityViewModels()
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentCardsBinding.inflate(i,c,false).also{_b=it}.root
    override fun onViewCreated(v: View, s: Bundle?) {
        val adapter = CardAdapter(
            onEdit = { card -> startActivity(Intent(requireContext(), AddEditCardActivity::class.java).apply {
                putExtra("id",card.id); putExtra("cardholderName",card.cardholderName)
                putExtra("cardNumber",card.cardNumber); putExtra("expiryDate",card.expiryDate)
                putExtra("cvv",card.cvv); putExtra("bankName",card.bankName); putExtra("cardType",card.cardType)
            })},
            onDelete = { vm.deleteCard(it) }
        )
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter
        vm.allCards.observe(viewLifecycleOwner) { adapter.submitList(it); b.emptyView.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}

class BankLoginsFragment : Fragment() {
    private var _b: FragmentBankLoginsBinding? = null
    private val b get() = _b!!
    private val vm: VaultViewModel by activityViewModels()
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentBankLoginsBinding.inflate(i,c,false).also{_b=it}.root
    override fun onViewCreated(v: View, s: Bundle?) {
        val adapter = BankLoginAdapter(
            onEdit = { l -> startActivity(Intent(requireContext(), AddEditLoginActivity::class.java).apply {
                putExtra("type","bank"); putExtra("id",l.id); putExtra("bankName",l.bankName)
                putExtra("accountNumber",l.accountNumber); putExtra("username",l.username)
                putExtra("password",l.password); putExtra("notes",l.notes)
            })},
            onDelete = { vm.deleteBankLogin(it) }
        )
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter
        vm.allBankLogins.observe(viewLifecycleOwner) { adapter.submitList(it); b.emptyView.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}

class WebLoginsFragment : Fragment() {
    private var _b: FragmentWebLoginsBinding? = null
    private val b get() = _b!!
    private val vm: VaultViewModel by activityViewModels()
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentWebLoginsBinding.inflate(i,c,false).also{_b=it}.root
    override fun onViewCreated(v: View, s: Bundle?) {
        val adapter = WebLoginAdapter(
            onEdit = { l -> startActivity(Intent(requireContext(), AddEditLoginActivity::class.java).apply {
                putExtra("type","web"); putExtra("id",l.id); putExtra("siteName",l.siteName)
                putExtra("url",l.url); putExtra("username",l.username)
                putExtra("password",l.password); putExtra("notes",l.notes)
            })},
            onDelete = { vm.deleteWebLogin(it) }
        )
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter
        vm.allWebLogins.observe(viewLifecycleOwner) { adapter.submitList(it); b.emptyView.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}

class NotesFragment : Fragment() {
    private var _b: FragmentNotesBinding? = null
    private val b get() = _b!!
    private val vm: VaultViewModel by activityViewModels()
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentNotesBinding.inflate(i,c,false).also{_b=it}.root
    override fun onViewCreated(v: View, s: Bundle?) {
        val adapter = NoteAdapter(
            onEdit = { n -> startActivity(Intent(requireContext(), AddEditNoteActivity::class.java).apply {
                putExtra("id",n.id); putExtra("title",n.title); putExtra("content",n.content)
            })},
            onDelete = { vm.deleteNote(it) }
        )
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter
        vm.allNotes.observe(viewLifecycleOwner) { adapter.submitList(it); b.emptyView.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
