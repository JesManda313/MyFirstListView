package com.example.myfirstlistview

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import androidx.core.view.GravityCompat

class MainActivity : AppCompatActivity() {

    var data = mutableListOf<String>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.main)
        navView = findViewById(R.id.nav_view)

        val btnOpenSidebar = findViewById<Button>(R.id.btnOpenSidebar)
        btnOpenSidebar.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        data.addAll(listOf("1", "2", "3", "4", "5"))
        val lvAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val lv1 = findViewById<ListView>(R.id.lv1)
        lv1.adapter = lvAdapter

        lv1.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, data[position], Toast.LENGTH_SHORT).show()
        }

        val gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = lv1.pointToPosition(e.x.toInt(), e.y.toInt())
                    if (position != ListView.INVALID_POSITION) {
                        val selectedItem = data[position]
                        showActionDialog(position, selectedItem, data, lvAdapter)
                    }
                    return true
                }
            }
        )
        lv1.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.popBackStack(null, 1)
                    Toast.makeText(this, "Kembali ke Home", Toast.LENGTH_SHORT).show()
                }
                R.id.bahanFragment -> {
                    val fragment = FragmentBahan()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.cartFragment -> {
                    val fragment = FragmentCart()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun showActionDialog(
        position: Int,
        selectedItem: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ITEM $selectedItem")
        builder.setMessage("Pilih Tindakan Yang Ingin Dilakukan: ")

        builder.setPositiveButton("Update") { _, _ ->
            showUpdateDialog(position, selectedItem, data, adapter)
        }
        builder.setNegativeButton("Hapus") { _, _ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Hapus Item $selectedItem", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun showUpdateDialog(
        position: Int,
        oldValue: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Data")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val tvOld = TextView(this)
        tvOld.text = "Data Lama: $oldValue"
        tvOld.textSize = 16f

        val etNew = EditText(this)
        etNew.hint = "Masukkan Data Baru"
        etNew.setText(oldValue)

        layout.addView(tvOld)
        layout.addView(etNew)

        builder.setView(layout)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newValue = etNew.text.toString().trim()
            if (newValue.isNotEmpty()) {
                data[position] = newValue
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Data diupdate jadi: $newValue", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Data baru tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
