package com.example.inventoryobat

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status") val status: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: T? = null
) {
    val isSuccess: Boolean get() = status == "success"
}
