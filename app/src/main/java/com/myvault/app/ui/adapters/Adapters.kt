package com.myvault.app.ui.adapters

import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.*
import com.myvault.app.R
import com.myvault.app.data.*

private fun mkHolder(parent: ViewGroup) = object : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_vault, parent, false)
) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
    val tvSub: TextView = itemView.findViewById(R.id.tvSub)
    val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
    val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
}

class CardAdapter(val onEdit:(Card)->Unit, val onDelete:(Card)->Unit) : ListAdapter<Card, RecyclerView.ViewHolder>(object:DiffUtil.ItemCallback<Card>(){
    override fun areItemsTheSame(a:Card,b:Card)=a.id==b.id
    override fun areContentsTheSame(a:Card,b:Card)=a==b
}) {
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = mkHolder(p)
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, i: Int) {
        val c=getItem(i)
        (h.itemView.findViewById<TextView>(R.id.tvTitle)).text="${c.bankName} • ${c.cardType}"
        (h.itemView.findViewById<TextView>(R.id.tvDetail)).text="**** **** **** ${c.cardNumber.takeLast(4)}"
        (h.itemView.findViewById<TextView>(R.id.tvSub)).text="${c.cardholderName}  Exp:${c.expiryDate}"
        h.itemView.findViewById<ImageButton>(R.id.btnEdit).setOnClickListener{onEdit(c)}
        h.itemView.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener{
            AlertDialog.Builder(h.itemView.context).setTitle("Delete Card?")
                .setPositiveButton("Delete"){_,_->onDelete(c)}.setNegativeButton("Cancel",null).show()
        }
    }
}

class BankLoginAdapter(val onEdit:(BankLogin)->Unit, val onDelete:(BankLogin)->Unit) : ListAdapter<BankLogin, RecyclerView.ViewHolder>(object:DiffUtil.ItemCallback<BankLogin>(){
    override fun areItemsTheSame(a:BankLogin,b:BankLogin)=a.id==b.id
    override fun areContentsTheSame(a:BankLogin,b:BankLogin)=a==b
}) {
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = mkHolder(p)
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, i: Int) {
        val l=getItem(i)
        (h.itemView.findViewById<TextView>(R.id.tvTitle)).text=l.bankName
        (h.itemView.findViewById<TextView>(R.id.tvDetail)).text="Acc: ${l.accountNumber}"
        (h.itemView.findViewById<TextView>(R.id.tvSub)).text="User: ${l.username}"
        h.itemView.findViewById<ImageButton>(R.id.btnEdit).setOnClickListener{onEdit(l)}
        h.itemView.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener{
            AlertDialog.Builder(h.itemView.context).setTitle("Delete Bank Login?")
                .setPositiveButton("Delete"){_,_->onDelete(l)}.setNegativeButton("Cancel",null).show()
        }
    }
}

class WebLoginAdapter(val onEdit:(WebLogin)->Unit, val onDelete:(WebLogin)->Unit) : ListAdapter<WebLogin, RecyclerView.ViewHolder>(object:DiffUtil.ItemCallback<WebLogin>(){
    override fun areItemsTheSame(a:WebLogin,b:WebLogin)=a.id==b.id
    override fun areContentsTheSame(a:WebLogin,b:WebLogin)=a==b
}) {
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = mkHolder(p)
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, i: Int) {
        val l=getItem(i)
        (h.itemView.findViewById<TextView>(R.id.tvTitle)).text=l.siteName
        (h.itemView.findViewById<TextView>(R.id.tvDetail)).text=l.url.ifBlank{"No URL"}
        (h.itemView.findViewById<TextView>(R.id.tvSub)).text="User: ${l.username}"
        h.itemView.findViewById<ImageButton>(R.id.btnEdit).setOnClickListener{onEdit(l)}
        h.itemView.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener{
            AlertDialog.Builder(h.itemView.context).setTitle("Delete Login?")
                .setPositiveButton("Delete"){_,_->onDelete(l)}.setNegativeButton("Cancel",null).show()
        }
    }
}

class NoteAdapter(val onEdit:(SecureNote)->Unit, val onDelete:(SecureNote)->Unit) : ListAdapter<SecureNote, RecyclerView.ViewHolder>(object:DiffUtil.ItemCallback<SecureNote>(){
    override fun areItemsTheSame(a:SecureNote,b:SecureNote)=a.id==b.id
    override fun areContentsTheSame(a:SecureNote,b:SecureNote)=a==b
}) {
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = mkHolder(p)
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, i: Int) {
        val n=getItem(i)
        (h.itemView.findViewById<TextView>(R.id.tvTitle)).text=n.title
        (h.itemView.findViewById<TextView>(R.id.tvDetail)).text=n.content.take(60)+(if(n.content.length>60)"..." else "")
        (h.itemView.findViewById<TextView>(R.id.tvSub)).text="🔒 Secure Note"
        h.itemView.findViewById<ImageButton>(R.id.btnEdit).setOnClickListener{onEdit(n)}
        h.itemView.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener{
            AlertDialog.Builder(h.itemView.context).setTitle("Delete Note?")
                .setPositiveButton("Delete"){_,_->onDelete(n)}.setNegativeButton("Cancel",null).show()
        }
    }
}
