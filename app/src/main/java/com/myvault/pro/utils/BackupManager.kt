package com.myvault.pro.utils

import android.content.Context
import android.os.Environment
import com.myvault.pro.data.VaultDatabase
import com.myvault.pro.data.VaultItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object BackupManager {

    fun backup(context: Context): String {
        return try {
            val items = VaultDatabase.get(context).vaultDao().getAllSync()
            val array = JSONArray()
            items.forEach { item ->
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("category", item.category)
                obj.put("title", item.title)
                obj.put("subtitle", item.subtitle)
                obj.put("field1", item.field1)
                obj.put("field2", item.field2)
                obj.put("field3", item.field3)
                obj.put("field4", item.field4)
                obj.put("notes", item.notes)
                obj.put("color", item.color)
                obj.put("createdAt", item.createdAt)
                array.put(obj)
            }
            val json = JSONObject()
            json.put("version", 1)
            json.put("backup_date", System.currentTimeMillis())
            json.put("items", array)

            val dateStr = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "VaultPro_backup_$dateStr.json"

            val dir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "VaultPro")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, fileName)
            file.writeText(json.toString(2))
            "Backup saved to Documents/VaultPro/$fileName"
        } catch (e: Exception) {
            "Backup failed: ${e.message}"
        }
    }

    fun restore(context: Context, filePath: String): String {
        return try {
            val file = File(filePath)
            if (!file.exists()) return "File not found"

            val json = JSONObject(file.readText())
            val array = json.getJSONArray("items")
            val dao = VaultDatabase.get(context).vaultDao()

            var count = 0
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val item = VaultItem(
                    category = obj.getString("category"),
                    title = obj.getString("title"),
                    subtitle = obj.optString("subtitle"),
                    field1 = obj.optString("field1"),
                    field2 = obj.optString("field2"),
                    field3 = obj.optString("field3"),
                    field4 = obj.optString("field4"),
                    notes = obj.optString("notes"),
                    color = obj.optString("color", "BLUE"),
                    createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                )
                kotlinx.coroutines.runBlocking { dao.insert(item) }
                count++
            }
            "Restored $count items successfully!"
        } catch (e: Exception) {
            "Restore failed: ${e.message}"
        }
    }
}
