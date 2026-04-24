package com.myvault.pro.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.myvault.pro.R
import com.myvault.pro.data.Categories
import com.myvault.pro.data.VaultItem

class AddItemActivity : AppCompatActivity() {

    private val vm: VaultViewModel by viewModels()
    private var editId = 0
    private var selectedCategory = "CARD"
    private var selectedColor = "BLUE"

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_add_item)

        editId = intent.getIntExtra("item_id", 0)
        selectedCategory = intent.getStringExtra("category") ?: "CARD"

        setupCategorySpinner()
        setupColorPicker()

        if (editId != 0) populateFields()

        findViewById<Button>(R.id.btnSave).setOnClickListener { save() }
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun setupCategorySpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerCategory)
        val cats = Categories.all
        val labels = cats.map { "${Categories.icon(it)} $it" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(cats.indexOf(selectedCategory).coerceAtLeast(0))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                selectedCategory = cats[pos]
                updateFormFields()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        updateFormFields()
    }

    private fun updateFormFields() {
        val l1 = findViewById<TextInputLayout>(R.id.layoutField1)
        val l2 = findViewById<TextInputLayout>(R.id.layoutField2)
        val l3 = findViewById<TextInputLayout>(R.id.layoutField3)
        val l4 = findViewById<TextInputLayout>(R.id.layoutField4)
        val lSub = findViewById<TextInputLayout>(R.id.layoutSubtitle)

        when (selectedCategory) {
            Categories.CARD -> {
                lSub.hint = "Card Number"; lSub.visibility = View.VISIBLE
                l1.hint = "Cardholder Name"; l1.visibility = View.VISIBLE
                l2.hint = "Expiry Date (MM/YY)"; l2.visibility = View.VISIBLE
                l3.hint = "CVV"; l3.visibility = View.VISIBLE
                l4.hint = "Card Type (VISA/MC/RuPay)"; l4.visibility = View.VISIBLE
            }
            Categories.BANK -> {
                lSub.hint = "Account Number"; lSub.visibility = View.VISIBLE
                l1.hint = "Username / Customer ID"; l1.visibility = View.VISIBLE
                l2.hint = "Password / MPIN"; l2.visibility = View.VISIBLE
                l3.hint = "IFSC Code"; l3.visibility = View.VISIBLE
                l4.hint = "Branch Name"; l4.visibility = View.VISIBLE
            }
            Categories.WEB -> {
                lSub.hint = "Username / Email"; lSub.visibility = View.VISIBLE
                l1.hint = "Password"; l1.visibility = View.VISIBLE
                l2.hint = "Website URL"; l2.visibility = View.VISIBLE
                l3.hint = "Recovery Email"; l3.visibility = View.VISIBLE
                l4.visibility = View.GONE
            }
            Categories.WIFI -> {
                lSub.hint = "Network Name (SSID)"; lSub.visibility = View.VISIBLE
                l1.hint = "Password"; l1.visibility = View.VISIBLE
                l2.hint = "Security Type (WPA2/WPA3)"; l2.visibility = View.VISIBLE
                l3.visibility = View.GONE; l4.visibility = View.GONE
            }
            Categories.ID -> {
                lSub.hint = "ID Number"; lSub.visibility = View.VISIBLE
                l1.hint = "Full Name"; l1.visibility = View.VISIBLE
                l2.hint = "Date of Issue"; l2.visibility = View.VISIBLE
                l3.hint = "Date of Expiry"; l3.visibility = View.VISIBLE
                l4.hint = "Issued By"; l4.visibility = View.VISIBLE
            }
            Categories.NOTE -> {
                lSub.visibility = View.GONE
                l1.visibility = View.GONE; l2.visibility = View.GONE
                l3.visibility = View.GONE; l4.visibility = View.GONE
            }
            Categories.CUSTOM -> {
                lSub.hint = "Field 1"; lSub.visibility = View.VISIBLE
                l1.hint = "Field 2"; l1.visibility = View.VISIBLE
                l2.hint = "Field 3"; l2.visibility = View.VISIBLE
                l3.hint = "Field 4"; l3.visibility = View.VISIBLE
                l4.visibility = View.GONE
            }
        }
    }

    private fun setupColorPicker() {
        val colors = listOf("BLUE","GREEN","AMBER","PURPLE","RED","TEAL")
        val hexColors = listOf("#4F8EF7","#22C55E","#F59E0B","#A855F7","#EF4444","#06B6D4")
        val container = findViewById<LinearLayout>(R.id.colorContainer)
        colors.forEachIndexed { idx, color ->
            val circle = View(this)
            val size = (32 * resources.displayMetrics.density).toInt()
            val params = LinearLayout.LayoutParams(size, size)
            params.marginEnd = 12
            circle.layoutParams = params
            circle.background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(android.graphics.Color.parseColor(hexColors[idx]))
            }
            circle.setOnClickListener {
                selectedColor = color
                container.children().forEach { it.alpha = 0.4f }
                circle.alpha = 1f
            }
            circle.alpha = if (color == selectedColor) 1f else 0.4f
            container.addView(circle)
        }
    }

    private fun LinearLayout.children() = (0 until childCount).map { getChildAt(it) }

    private fun populateFields() {
        f(R.id.etTitle).setText(intent.getStringExtra("title"))
        f(R.id.etSubtitle).setText(intent.getStringExtra("subtitle"))
        f(R.id.etField1).setText(intent.getStringExtra("field1"))
        f(R.id.etField2).setText(intent.getStringExtra("field2"))
        f(R.id.etField3).setText(intent.getStringExtra("field3"))
        f(R.id.etField4).setText(intent.getStringExtra("field4"))
        f(R.id.etNotes).setText(intent.getStringExtra("notes"))
        selectedColor = intent.getStringExtra("color") ?: "BLUE"
        findViewById<TextView>(R.id.tvHeader).text = "Edit ${Categories.icon(selectedCategory)} $selectedCategory"
    }

    private fun f(id: Int) = findViewById<TextInputEditText>(id)
    private fun t(id: Int) = f(id).text.toString().trim()

    private fun save() {
        val title = t(R.id.etTitle)
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show(); return
        }
        val item = VaultItem(
            id = editId,
            category = selectedCategory,
            title = title,
            subtitle = t(R.id.etSubtitle),
            field1 = t(R.id.etField1),
            field2 = t(R.id.etField2),
            field3 = t(R.id.etField3),
            field4 = t(R.id.etField4),
            notes = t(R.id.etNotes),
            color = selectedColor
        )
        if (editId == 0) vm.insert(item) else vm.update(item)
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
