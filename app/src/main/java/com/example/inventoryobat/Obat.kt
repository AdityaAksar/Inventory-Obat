package com.example.inventoryobat

import com.google.gson.annotations.SerializedName

data class Obat(
    @JvmField @SerializedName("id_obat") val idObat: Int = 0,
    @JvmField @SerializedName("nama_obat") val namaObat: String = "",
    @JvmField @SerializedName("jenis_obat") val jenisObat: String = "",
    @JvmField @SerializedName("stock") val stock: Int = 0,
    @SerializedName("gambar") val gambar: String = "",
    @JvmField @SerializedName("gambar_url") val gambarUrl: String = "",
    @JvmField @SerializedName("id_supplier") val idSupplier: Int = 0,
    @JvmField @SerializedName("supplier") val supplier: Supplier? = null,
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = ""
)
