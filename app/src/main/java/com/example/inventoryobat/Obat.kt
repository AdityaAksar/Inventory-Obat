package com.example.inventoryobat

import com.google.gson.annotations.SerializedName

data class Obat(
    @SerializedName("id_obat") val idObat: Int = 0,
    @SerializedName("nama_obat") val namaObat: String = "",
    @SerializedName("jenis_obat") val jenisObat: String = "",
    @SerializedName("stock") val stock: Int = 0,
    @SerializedName("gambar") val gambar: String = "",
    @SerializedName("gambar_url") val gambarUrl: String = "",
    @SerializedName("id_supplier") val idSupplier: Int = 0,
    @SerializedName("supplier") val supplier: Supplier? = null,
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = ""
)
