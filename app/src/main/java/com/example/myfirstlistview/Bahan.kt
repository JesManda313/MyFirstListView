package com.example.myfirstlistview

data class Bahan (
    var nama: String,
    var kategori: String,
    var imageUrl: String = ""
){
    override fun toString(): String {
        return "$nama ($kategori)"
    }
}