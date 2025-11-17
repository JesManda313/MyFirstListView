package com.example.myfirstlistview

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(context: Context) {
    private val sp: SharedPreferences = context.getSharedPreferences("AppBahanMasakan", Context.MODE_PRIVATE)
    private val gson = Gson()
    fun saveBahanList(list: MutableList<Bahan>) {
        val jsonString = gson.toJson(list)
        val editor = sp.edit()
        editor.putString("dt_bahan", jsonString)
        editor.apply()
    }

    fun loadBahanList(): MutableList<Bahan> {
        val jsonString = sp.getString("dt_bahan", null)

        return if (jsonString != null) {
            val type = object : TypeToken<MutableList<Bahan>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            mutableListOf()
        }
    }
    fun clearData() {
        sp.edit().clear().apply()
    }
}