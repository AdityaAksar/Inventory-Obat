package com.example.inventoryobat;

public class Obat {
    private int idObat;
    private String namaObat;
    private JenisObat jenisObat;
    private int stock;
    private String gambar;
    private int idSupplier;
    private Supplier supplier;

    public Obat(){

    }
    public Obat(int idObat, String namaObat, JenisObat jenisObat, int stock, String gambar, int idSupplier, Supplier supplier) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.jenisObat = jenisObat;
        this.stock = stock;
        this.gambar = gambar;
        this.idSupplier = idSupplier;
        this.supplier = supplier;
    }

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

    public JenisObat getJenisObat() {
        return jenisObat;
    }

    public void setJenisObat(JenisObat jenisObat) {
        this.jenisObat = jenisObat;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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
}


