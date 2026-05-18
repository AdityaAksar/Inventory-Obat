package com.example.inventoryobat

import com.google.gson.annotations.SerializedName

data class Supplier(
    @SerializedName("id_supplier") val idSupplier: Int = 0,
    @SerializedName("nama_supplier") val namaSupplier: String = "",
    @SerializedName("nomor") val nomor: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = ""
) {
    override fun toString(): String = namaSupplier
}
