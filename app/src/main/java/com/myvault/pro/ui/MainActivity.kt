package com.myvault.pro.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myvault.pro.R
import com.myvault.pro.data.Categories
import com.myvault.pro.data.VaultItem
import com.myvault.pro.ui.adapters.VaultAdapter

class MainActivity : AppCompatActivity() {

    private val vm: VaultViewModel by viewModels()
    private lateinit var adapter: VaultAdapter
    private lateinit var rvItems: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var etSearch: EditText
    private var currentCat = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvItems = findViewById(R.id.rvItems)
        tvEmpty = findViewById(R.id.tvEmpty)
        etSearch = findViewById(R.id.etSearch)

        setupAdapter()
        setupCategoryTabs()
        setupSearch()

        // FAB
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
            .setOnClickListener {
                startActivity(Intent(this, AddItemActivity::class.java).apply {
                    putExtra("category", currentCat.takeIf { it != "ALL" } ?: "CARD")
                })
            }

        // Settings
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Load all items
        observeCategory("ALL")
    }

    private fun setupAdapter() {
        adapter = VaultAdapter(
            onEdit = { item ->
                startActivity(Intent(this, AddItemActivity::class.java).apply {
                    putExtra("item_id", item.id)
                    putExtra("category", item.category)
                    putExtra("title", item.title)
                    putExtra("subtitle", item.subtitle)
                    putExtra("field1", item.field1)
                    putExtra("field2", item.field2)
                    putExtra("field3", item.field3)
                    putExtra("field4", item.field4)
                    putExtra("notes", item.notes)
                    putExtra("color", item.color)
                })
            },
            onDelete = { item -> vm.delete(item) },
            onCopy = { text ->
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clipboard.setPrimaryClip(android.content.ClipData.newPlainText("copied", text))
                Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
            }
        )
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter
    }

    private fun setupCategoryTabs() {
        val tabLayout = findViewById<HorizontalScrollView>(R.id.scrollTabs)
        val tabContainer = findViewById<LinearLayout>(R.id.tabContainer)

        val tabs = listOf("ALL") + Categories.all
        tabs.forEach { cat ->
            val btn = Button(this)
            btn.text = if (cat == "ALL") "All" else "${Categories.icon(cat)} $cat"
            btn.textSize = 12f
            btn.setPadding(32, 16, 32, 16)
            btn.background = getDrawable(R.drawable.tab_unselected)
            btn.setTextColor(getColor(R.color.text_secondary))
            btn.setOnClickListener {
                currentCat = cat
                observeCategory(cat)
                updateTabSelection(tabContainer, btn)
            }
            if (cat == "ALL") {
                btn.background = getDrawable(R.drawable.tab_selected)
                btn.setTextColor(getColor(android.R.color.white))
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginEnd = 8
            tabContainer.addView(btn, params)
        }
    }

    private fun updateTabSelection(container: LinearLayout, selected: Button) {
        for (i in 0 until container.childCount) {
            val btn = container.getChildAt(i) as Button
            if (btn == selected) {
                btn.background = getDrawable(R.drawable.tab_selected)
                btn.setTextColor(getColor(android.R.color.white))
            } else {
                btn.background = getDrawable(R.drawable.tab_unselected)
                btn.setTextColor(getColor(R.color.text_secondary))
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s.toString().trim()
                if (q.isEmpty()) {
                    observeCategory(currentCat)
                } else {
                    vm.search(q).observe(this@MainActivity) { items ->
                        adapter.submitList(items)
                        tvEmpty.visibility = if (items.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })
    }

    private fun observeCategory(cat: String) {
        if (cat == "ALL") {
            vm.allItems.observe(this) { items ->
                adapter.submitList(items)
                tvEmpty.visibility = if (items.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        } else {
            vm.getByCategory(cat).observe(this) { items ->
                adapter.submitList(items)
                tvEmpty.visibility = if (items.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }
}
