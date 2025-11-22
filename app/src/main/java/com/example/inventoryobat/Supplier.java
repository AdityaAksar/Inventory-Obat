package com.example.inventoryobat;

import com.google.gson.annotations.SerializedName;

public class Supplier {
    @SerializedName("id_supplier")
    private int idSupplier;

    @SerializedName("nama_supplier")
    private String namaSupplier;

    @SerializedName("nomor")
    private String nomor;

    @SerializedName("email")
    private String email;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Constructors
    public Supplier() {}

    public Supplier(String namaSupplier, String nomor, String email) {
        this.namaSupplier = namaSupplier;
        this.nomor = nomor;
        this.email = email;
    }

    // Getters & Setters
    public int getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return namaSupplier;
    }
}
