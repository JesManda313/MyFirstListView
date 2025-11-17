package com.example.myfirstlistview

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(context: Context) {
    private val sp: SharedPreferences = context.getSharedPreferences("AppBahanMasakan", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveData(key: String, list: MutableList<Bahan>) {
        val jsonString = gson.toJson(list)
        sp.edit().putString(key, jsonString).apply()
    }

    fun loadData(key: String): MutableList<Bahan> {
        val jsonString = sp.getString(key, null)
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