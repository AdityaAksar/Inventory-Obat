package com.example.inventoryobat

import com.google.gson.annotations.SerializedName

data class UpdateStockRequest(
    @SerializedName("stock") val stock: Int
)
