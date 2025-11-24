package com.example.inventoryobat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inventoryobat.databinding.ActivityInfoProdukBinding;

public class InfoProdukActivity extends AppCompatActivity {

    private ActivityInfoProdukBinding binding;
    private MainViewModel viewModel;
    private Obat currentObat;
    private int quantity = 0;
    private int obatId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoProdukBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainViewModel();

        obatId = getIntent().getIntExtra("obat_id", -1);

        if (obatId == -1) {
            Toast.makeText(this, "Data obat tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.actionEdit.setOnClickListener(v -> {
            if (currentObat != null) {
                Intent editIntent = new Intent(this, TambahObatActivity.class);
                editIntent.putExtra("edit_mode", true);
                editIntent.putExtra("obat_id", currentObat.getIdObat());
                startActivity(editIntent);
            }
        });

        binding.actionDelete.setOnClickListener(v -> {
            if (currentObat != null) {
                viewModel.deleteObat(currentObat.getIdObat());
                Toast.makeText(this, "Obat dihapus", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        binding.btnKurang.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                binding.edtQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.btnTambah.setOnClickListener(v -> {
            quantity++;
            binding.edtQuantity.setText(String.valueOf(quantity));
        });

        binding.btnUpdateStock.setOnClickListener(v -> updateStock());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (obatId != -1) {
            loadObatDetail(obatId);
        }
    }

    private void loadObatDetail(int id) {
        viewModel.getObatById(id).observe(this, obat -> {
            if (obat != null) {
                currentObat = obat;
                binding.tvNamaObatDetail.setText(obat.getNamaObat());
                binding.tvJenisObatDetail.setText("Jenis: " + currentObat.getJenisObat());
                binding.tvStockDetail.setText("Stock: " + obat.getStock());

                if (obat.getSupplier() != null) {
                    binding.tvSupplierDetail.setText("Supplier: " + obat.getSupplier().getNamaSupplier());
                    binding.tvEmailSupplier.setText("Email: " + obat.getSupplier().getEmail());
                    binding.tvNomorSupplier.setText("Nomor: " + obat.getSupplier().getNomor());
                }

                if (obat.getGambarUrl() != null && !obat.getGambarUrl().isEmpty()) {
                    Glide.with(this)
                            .load(obat.getGambarUrl())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(binding.imgObatDetail);
                }
            }
        });
    }

    private void updateStock() {
        if (currentObat == null) return;

        String quantityStr = binding.edtQuantity.getText().toString().trim();

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Masukkan jumlah quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int qty = Integer.parseInt(quantityStr);
        int newStock = currentObat.getStock() + qty;

        if (newStock >= 0) {
            viewModel.updateStock(currentObat.getIdObat(), newStock);
            Toast.makeText(this, "Stock berhasil diupdate", Toast.LENGTH_SHORT).show();
            loadObatDetail(currentObat.getIdObat());
            quantity = 0;
            binding.edtQuantity.setText("0");

        } else {
            Toast.makeText(this, "Stock tidak boleh negatif", Toast.LENGTH_SHORT).show();
        }
    }
}