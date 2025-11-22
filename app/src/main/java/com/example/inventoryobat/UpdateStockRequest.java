package com.example.inventoryobat;

import com.google.gson.annotations.SerializedName;

public class UpdateStockRequest {
    @SerializedName("stock")
    private int stock;

    public UpdateStockRequest(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
