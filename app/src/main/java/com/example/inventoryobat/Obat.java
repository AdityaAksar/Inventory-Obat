package com.example.inventoryobat;

import com.google.gson.annotations.SerializedName;

public class Obat {
    @SerializedName("id_obat")
    private int idObat;

    @SerializedName("nama_obat")
    private String namaObat;

    @SerializedName("jenis_obat")
    private String jenisObat;

    @SerializedName("stock")
    private int stock;

    @SerializedName("gambar")
    private String gambar;

    @SerializedName("gambar_url")
    private String gambarUrl;

    @SerializedName("id_supplier")
    private int idSupplier;

    @SerializedName("supplier")
    private Supplier supplier;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Constructors
    public Obat() {}

    public Obat(String namaObat, String jenisObat, int stock, int idSupplier) {
        this.namaObat = namaObat;
        this.jenisObat = jenisObat;
        this.stock = stock;
        this.idSupplier = idSupplier;
    }

    // Getters & Setters
    public int getIdObat() {
        return idObat;
    }

    public void setIdObat(int idObat) {
        this.idObat = idObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getJenisObat() {
        return jenisObat;
    }

    public void setJenisObat(String jenisObat) {
        this.jenisObat = jenisObat;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public void setGambarUrl(String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    public int getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
}
