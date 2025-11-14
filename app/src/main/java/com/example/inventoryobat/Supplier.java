package com.example.inventoryobat;

public class Supplier {
    private int idSupplier;
    private String namaSupplier;
    private String nomor;
    private String email;

    public Supplier(){

    }


    public Supplier(int idSupplier, String namaSupplier, String nomor, String email) {
        this.idSupplier = idSupplier;
        this.namaSupplier = namaSupplier;
        this.nomor = nomor;
        this.email = email;
    }

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
}

