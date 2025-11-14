package com.example.inventoryobat;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private DatabaseHelper databaseHelper;
    private MutableLiveData<ArrayList<Obat>> obatListLiveData;
    private MutableLiveData<ArrayList<Supplier>> supplierListLiveData;
    private MutableLiveData<Boolean> loginStatus;

    public MainViewModel(Context context) {
        databaseHelper = new DatabaseHelper(context);
        obatListLiveData = new MutableLiveData<>();
        supplierListLiveData = new MutableLiveData<>();
        loginStatus = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public void login(String username, String password) {
        boolean result = databaseHelper.checkUser(username, password);
        loginStatus.setValue(result);
    }

    public LiveData<ArrayList<Obat>> getObatList() {
        return obatListLiveData;
    }

    public void loadAllObat() {
        ArrayList<Obat> list = databaseHelper.getAllObat();
        obatListLiveData.setValue(list);
    }

    public void loadObatByJenis(String jenisObat) {
        ArrayList<Obat> list = databaseHelper.getObatByJenis(jenisObat);
        obatListLiveData.setValue(list);
    }

    public void insertObat(Obat obat) {
        long result = databaseHelper.insertObat(obat);
        if (result != -1) {
            loadAllObat();
        }
    }

    public void updateStock(int idObat, int newStock) {
        databaseHelper.updateStock(idObat, newStock);
        loadAllObat();
    }

    public LiveData<ArrayList<Supplier>> getSupplierList() {
        return supplierListLiveData;
    }

    public void loadAllSuppliers() {
        ArrayList<Supplier> list = databaseHelper.getAllSuppliers();
        supplierListLiveData.setValue(list);
    }
    public void updateObat(Obat obat) {
        databaseHelper.updateObat(obat);
    }

    public void deleteObat(int idObat) {
        databaseHelper.deleteObat(idObat);
    }

}

