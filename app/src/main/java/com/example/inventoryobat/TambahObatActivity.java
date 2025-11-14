package com.example.inventoryobat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryobat.databinding.ActivityTambahObatBinding;

import java.util.ArrayList;

public class TambahObatActivity extends AppCompatActivity {

    private ActivityTambahObatBinding binding;
    private MainViewModel viewModel;
    private Uri selectedImageUri;
    private ArrayList<Supplier> supplierList;
    private int selectedSupplierId;
    private static final int PICK_IMAGE_REQUEST = 1;

    // Variabel untuk mode edit
    private boolean isEditMode = false;
    private int editObatId;
    private Obat editObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTambahObatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainViewModel(this);

        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this);
        }

        checkEditMode();

        setupJenisObatSpinner();
        setupSupplierSpinner();

        binding.btnPilihGambar.setOnClickListener(v -> {
            openImagePicker();
        });

        binding.btnSimpan.setOnClickListener(v -> {
            if (isEditMode) {
                updateObat();
            } else {
                simpanObat();
            }
        });
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        if ("edit".equals(mode)) {
            isEditMode = true;
            editObatId = intent.getIntExtra("obat_id", -1);

            if (editObatId != -1) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Edit Obat");
                }

                loadDataForEdit(intent);
            }
        } else {
            isEditMode = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Tambah Obat");
            }
        }
    }

    private void loadDataForEdit(Intent intent) {
        String namaObat = intent.getStringExtra("nama_obat");
        String jenisObat = intent.getStringExtra("jenis_obat");
        int stock = intent.getIntExtra("stock", 0);
        String gambar = intent.getStringExtra("gambar");
        selectedSupplierId = intent.getIntExtra("id_supplier", 0);

        binding.edtNamaObat.setText(namaObat);
        binding.edtStock.setText(String.valueOf(stock));

        if (gambar != null && !gambar.isEmpty()) {
            selectedImageUri = Uri.parse(gambar);
            binding.imgPreview.setImageURI(selectedImageUri);
        }

        editObat = new Obat();
        editObat.setIdObat(editObatId);
        editObat.setNamaObat(namaObat);
        editObat.setJenisObat(JenisObat.valueOf(jenisObat));
        editObat.setStock(stock);
        editObat.setGambar(gambar);
        editObat.setIdSupplier(selectedSupplierId);
    }

    private void setupJenisObatSpinner() {
        ArrayList<String> jenisObatList = new ArrayList<>();
        for (JenisObat jenis : JenisObat.values()) {
            jenisObatList.add(jenis.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                jenisObatList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerJenisObat.setAdapter(adapter);

        if (isEditMode && editObat != null) {
            int position = editObat.getJenisObat().ordinal();
            binding.spinnerJenisObat.setSelection(position);
        }
    }

    private void setupSupplierSpinner() {
        viewModel.loadAllSuppliers();
        viewModel.getSupplierList().observe(this, suppliers -> {
            supplierList = suppliers;
            ArrayList<String> supplierNames = new ArrayList<>();
            for (Supplier supplier : suppliers) {
                supplierNames.add(supplier.getNamaSupplier());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    supplierNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerSupplier.setAdapter(adapter);

            binding.spinnerSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSupplierId = supplierList.get(position).getIdSupplier();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (isEditMode && editObat != null) {
                for (int i = 0; i < supplierList.size(); i++) {
                    if (supplierList.get(i).getIdSupplier() == editObat.getIdSupplier()) {
                        binding.spinnerSupplier.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void openImagePicker() {
        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this);
            Toast.makeText(this, "Berikan izin akses media terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin diberikan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Izin ditolak. Aplikasi memerlukan izin untuk memilih gambar",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                getContentResolver().takePersistableUriPermission(
                        selectedImageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            binding.imgPreview.setImageURI(selectedImageUri);
        }
    }

    private void simpanObat() {
        String namaObat = binding.edtNamaObat.getText().toString().trim();
        String stockStr = binding.edtStock.getText().toString().trim();

        if (namaObat.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        int jenisPosition = binding.spinnerJenisObat.getSelectedItemPosition();
        JenisObat jenisObat = JenisObat.values()[jenisPosition];

        Obat obat = new Obat();
        obat.setNamaObat(namaObat);
        obat.setJenisObat(jenisObat);
        obat.setStock(stock);
        obat.setIdSupplier(selectedSupplierId);

        if (selectedImageUri != null) {
            obat.setGambar(selectedImageUri.toString());
        }

        viewModel.insertObat(obat);
        Toast.makeText(this, "Obat berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateObat() {
        String namaObat = binding.edtNamaObat.getText().toString().trim();
        String stockStr = binding.edtStock.getText().toString().trim();

        if (namaObat.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        int jenisPosition = binding.spinnerJenisObat.getSelectedItemPosition();
        JenisObat jenisObat = JenisObat.values()[jenisPosition];

        Obat obat = new Obat();
        obat.setIdObat(editObatId);
        obat.setNamaObat(namaObat);
        obat.setJenisObat(jenisObat);
        obat.setStock(stock);
        obat.setIdSupplier(selectedSupplierId);

        if (selectedImageUri != null) {
            obat.setGambar(selectedImageUri.toString());
        } else if (editObat != null && editObat.getGambar() != null) {
            obat.setGambar(editObat.getGambar());
        }

        viewModel.updateObat(obat);
        Toast.makeText(this, "Obat berhasil diupdate!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
