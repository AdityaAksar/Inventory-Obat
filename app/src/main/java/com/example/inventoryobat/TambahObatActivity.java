package com.example.inventoryobat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inventoryobat.databinding.ActivityTambahObatBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahObatActivity extends AppCompatActivity {

    private ActivityTambahObatBinding binding;
    private MainViewModel viewModel;
    private ApiService apiService;
    private Uri selectedImageUri;
    private List<Supplier> supplierList = new ArrayList<>();
    private int selectedSupplierId;
    private boolean isEdit = false;
    private int editObatId = -1;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTambahObatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainViewModel();
        apiService = ApiConfig.getApiService();

        isEdit = getIntent().getBooleanExtra("edit_mode", false);
        if (isEdit) {
            editObatId = getIntent().getIntExtra("obat_id", -1);
            binding.btnSimpan.setText("UPDATE OBAT");
        }

        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this);
        }

        setupJenisObatSpinner();
        setupSupplierSpinner();

        if (isEdit) {
            new Handler().postDelayed(this::loadDataForEdit, 200);
        }

        binding.btnPilihGambar.setOnClickListener(v -> openImagePicker());
        binding.btnSimpan.setOnClickListener(v -> simpanObat());
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
    }

    private void setupSupplierSpinner() {
        viewModel.loadAllSuppliers();
        viewModel.getSupplierList().observe(this, suppliers -> {
            supplierList = suppliers;
            ArrayList<String> names = new ArrayList<>();
            for (Supplier s : suppliers) {
                names.add(s.getNamaSupplier());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    names
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerSupplier.setAdapter(adapter);

            binding.spinnerSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0 && position < supplierList.size()) {
                        selectedSupplierId = supplierList.get(position).getIdSupplier();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        });
    }

    private void loadDataForEdit() {
        if (editObatId < 0) return;

        viewModel.getObatById(editObatId).observe(this, obat -> {
            if (obat == null) return;

            binding.edtNamaObat.setText(obat.getNamaObat());
            binding.edtStock.setText(String.valueOf(obat.getStock()));

            String jenisObat = obat.getJenisObat();
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spinnerJenisObat.getAdapter();
            if (adapter != null) {
                int position = adapter.getPosition(jenisObat);
                binding.spinnerJenisObat.setSelection(position >= 0 ? position : 0);
            }

            for (int i = 0; i < supplierList.size(); i++) {
                if (supplierList.get(i).getIdSupplier() == obat.getIdSupplier()) {
                    binding.spinnerSupplier.setSelection(i);
                    selectedSupplierId = obat.getIdSupplier();
                    break;
                }
            }

            if (obat.getGambarUrl() != null && !obat.getGambarUrl().isEmpty()) {
                Glide.with(this)
                        .load(obat.getGambarUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(binding.imgPreview);
            }
        });
    }

    private void openImagePicker() {
        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this);
            Toast.makeText(this, "Izin akses media diperlukan", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Izin diberikan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                selectedImageUri = uri;
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.imgPreview.setImageURI(uri);
            }
        }
    }

    private void simpanObat() {
        String nama = binding.edtNamaObat.getText().toString().trim();
        String stockStr = binding.edtStock.getText().toString().trim();

        if (nama.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        String jenis = (String) binding.spinnerJenisObat.getSelectedItem();

        RequestBody namaBody = RequestBody.create(nama, MediaType.parse("text/plain"));
        RequestBody jenisBody = RequestBody.create(jenis, MediaType.parse("text/plain"));
        RequestBody stockBody = RequestBody.create(String.valueOf(stock), MediaType.parse("text/plain"));
        RequestBody supplierBody = RequestBody.create(String.valueOf(selectedSupplierId), MediaType.parse("text/plain"));

        MultipartBody.Part gambarPart = null;
        if (selectedImageUri != null) {
            File file = uriToFile(selectedImageUri);
            if (file != null) {
                RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
                gambarPart = MultipartBody.Part.createFormData("gambar", file.getName(), requestBody);
            }
        }

        if (isEdit) {
            apiService.updateObat(editObatId, namaBody, jenisBody, stockBody, supplierBody, gambarPart)
                .enqueue(new Callback<ApiResponse<Obat>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Obat>> call, Response<ApiResponse<Obat>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TambahObatActivity.this, "Obat berhasil diupdate!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(TambahObatActivity.this, "Gagal update obat: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Obat>> call, Throwable t) {
                        Toast.makeText(TambahObatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            apiService.createObat(namaBody, jenisBody, stockBody, supplierBody, gambarPart)
                .enqueue(new Callback<ApiResponse<Obat>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Obat>> call, Response<ApiResponse<Obat>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TambahObatActivity.this, "Obat berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(TambahObatActivity.this, "Gagal tambah obat: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Obat>> call, Throwable t) {
                        Toast.makeText(TambahObatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private File uriToFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), "temp_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}