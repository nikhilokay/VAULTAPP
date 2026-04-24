package com.myvault.pro.ui.adapters

import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myvault.pro.R
import com.myvault.pro.data.Categories
import com.myvault.pro.data.VaultItem

class VaultAdapter(
    private val onEdit: (VaultItem) -> Unit,
    private val onDelete: (VaultItem) -> Unit,
    private val onCopy: (String) -> Unit
) : ListAdapter<VaultItem, VaultAdapter.VH>(DIFF()) {

    class DIFF : DiffUtil.ItemCallback<VaultItem>() {
        override fun areItemsTheSame(a: VaultItem, b: VaultItem) = a.id == b.id
        override fun areContentsTheSame(a: VaultItem, b: VaultItem) = a == b
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val accent: View = v.findViewById(R.id.vAccent)
        val tvIcon: TextView = v.findViewById(R.id.tvIcon)
        val tvCategory: TextView = v.findViewById(R.id.tvCategory)
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = v.findViewById(R.id.tvSubtitle)
        val tvField1Label: TextView = v.findViewById(R.id.tvField1Label)
        val tvField1: TextView = v.findViewById(R.id.tvField1)
        val btnCopy: Button = v.findViewById(R.id.btnCopy)
        val btnShow: Button = v.findViewById(R.id.btnShow)
        val btnEdit: ImageButton = v.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
        var passwordShown = false
        var actualPassword = ""
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_vault, p, false))

    override fun onBindViewHolder(h: VH, i: Int) {
        val item = getItem(i)
        val colorHex = Categories.color(item.category)

        try { h.accent.setBackgroundColor(Color.parseColor(colorHex)) } catch (e: Exception) {}

        h.tvIcon.text = Categories.icon(item.category)
        h.tvCategory.text = item.category
        h.tvTitle.text = item.title

        // Subtitle (card number, username, ssid etc)
        if (item.subtitle.isNotEmpty()) {
            h.tvSubtitle.visibility = View.VISIBLE
            h.tvSubtitle.text = when (item.category) {
                Categories.CARD -> maskCardNumber(item.subtitle)
                else -> item.subtitle
            }
        } else {
            h.tvSubtitle.visibility = View.GONE
        }

        // Password/secret field
        val secretField = item.field1
        val secretLabel = when (item.category) {
            Categories.CARD -> "CVV"
            Categories.BANK -> "Password"
            Categories.WEB -> "Password"
            Categories.WIFI -> "Password"
            else -> ""
        }

        if (secretField.isNotEmpty() && secretLabel.isNotEmpty()) {
            h.tvField1Label.visibility = View.VISIBLE
            h.tvField1.visibility = View.VISIBLE
            h.btnCopy.visibility = View.VISIBLE
            h.btnShow.visibility = View.VISIBLE
            h.tvField1Label.text = secretLabel
            h.actualPassword = secretField
            h.passwordShown = false
            h.tvField1.text = "••••••••"

            h.btnCopy.setOnClickListener { onCopy(secretField) }
            h.btnShow.setOnClickListener {
                h.passwordShown = !h.passwordShown
                h.tvField1.text = if (h.passwordShown) secretField else "••••••••"
                h.btnShow.text = if (h.passwordShown) "Hide" else "Show"
            }
        } else {
            h.tvField1Label.visibility = View.GONE
            h.tvField1.visibility = View.GONE
            h.btnCopy.visibility = View.GONE
            h.btnShow.visibility = View.GONE
        }

        h.btnEdit.setOnClickListener { onEdit(item) }
        h.btnDelete.setOnClickListener {
            AlertDialog.Builder(h.itemView.context)
                .setTitle("Delete '${item.title}'?")
                .setMessage("This cannot be undone.")
                .setPositiveButton("Delete") { _, _ -> onDelete(item) }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun maskCardNumber(num: String): String {
        return if (num.length >= 4) "**** **** **** ${num.takeLast(4)}"
        else num
    }
}
