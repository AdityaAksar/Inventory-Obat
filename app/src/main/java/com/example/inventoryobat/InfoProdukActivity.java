package com.example.inventoryobat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryobat.databinding.ActivityInfoProdukBinding;

public class InfoProdukActivity extends AppCompatActivity {

    private ActivityInfoProdukBinding binding;
    private MainViewModel viewModel;
    private Obat currentObat;
    private int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoProdukBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainViewModel(this);

        int obatId = getIntent().getIntExtra("obat_id", -1);
        if (obatId != -1) {
            loadObatDetail(obatId);
        } else {
            Toast.makeText(this, "Data obat tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

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

        binding.btnUpdateStock.setOnClickListener(v -> {
            updateStock();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info_produk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            editObat();
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadObatDetail(int obatId) {
        DatabaseHelper db = new DatabaseHelper(this);
        currentObat = db.getObatById(obatId);

        if (currentObat != null) {
            binding.tvNamaObatDetail.setText(currentObat.getNamaObat());
            binding.tvJenisObatDetail.setText("Jenis: " + currentObat.getJenisObat().getDisplayName());
            binding.tvStockDetail.setText("Stock: " + currentObat.getStock());

            if (currentObat.getSupplier() != null) {
                Supplier supplier = currentObat.getSupplier();
                binding.tvSupplierDetail.setText("Supplier: " + supplier.getNamaSupplier());
                binding.tvEmailSupplier.setText("Email: " + supplier.getEmail());
                binding.tvNomorSupplier.setText("Nomor: " + supplier.getNomor());
            }

            if (currentObat.getGambar() != null && !currentObat.getGambar().isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(currentObat.getGambar());
                    binding.imgObatDetail.setImageURI(imageUri);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    binding.imgObatDetail.setImageResource(R.drawable.ic_launcher_foreground);
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.imgObatDetail.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            Toast.makeText(this, "Data obat tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateStock() {
        if (currentObat == null) {
            Toast.makeText(this, "Data obat tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

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
            finish();
        } else {
            Toast.makeText(this, "Stock tidak boleh negatif", Toast.LENGTH_SHORT).show();
        }
    }

    private void editObat() {
        if (currentObat == null) {
            Toast.makeText(this, "Data obat tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, TambahObatActivity.class);
        intent.putExtra("mode", "edit");
        intent.putExtra("obat_id", currentObat.getIdObat());
        intent.putExtra("nama_obat", currentObat.getNamaObat());
        intent.putExtra("jenis_obat", currentObat.getJenisObat().name());
        intent.putExtra("stock", currentObat.getStock());
        intent.putExtra("gambar", currentObat.getGambar());
        intent.putExtra("id_supplier", currentObat.getIdSupplier());
        startActivity(intent);
        finish();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus obat " + currentObat.getNamaObat() + "?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteObat();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteObat() {
        if (currentObat == null) {
            Toast.makeText(this, "Data obat tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.deleteObat(currentObat.getIdObat());
        Toast.makeText(this, "Obat berhasil dihapus", Toast.LENGTH_SHORT).show();
        finish(); // Kembali ke halaman sebelumnya
    }
}
