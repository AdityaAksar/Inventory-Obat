package com.example.inventoryobat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inventoryobat.databinding.ActivityInfoProdukBinding

class InfoProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoProdukBinding
    private lateinit var viewModel: MainViewModel
    private var currentObat: Obat? = null
    private var quantity = 0
    private var obatId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = MainViewModel()

        obatId = intent.getIntExtra("obat_id", -1)

        if (obatId == -1) {
            Toast.makeText(this, "Data obat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.actionEdit.setOnClickListener {
            currentObat?.let { obat ->
                val editIntent = Intent(this, TambahObatActivity::class.java).apply {
                    putExtra("edit_mode", true)
                    putExtra("obat_id", obat.idObat)
                }
                startActivity(editIntent)
            }
        }

        binding.actionDelete.setOnClickListener {
            currentObat?.let { obat ->
                viewModel.deleteObat(obat.idObat)
                Toast.makeText(this, "Obat dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnKurang.setOnClickListener {
            if (quantity > 0) {
                quantity--
                binding.edtQuantity.setText(quantity.toString())
            }
        }

        binding.btnTambah.setOnClickListener {
            quantity++
            binding.edtQuantity.setText(quantity.toString())
        }

        binding.btnUpdateStock.setOnClickListener { updateStock() }
    }

    override fun onResume() {
        super.onResume()
        if (obatId != -1) {
            loadObatDetail(obatId)
        }
    }

    private fun loadObatDetail(id: Int) {
        viewModel.getObatById(id).observe(this) { obat ->
            obat ?: return@observe
            currentObat = obat
            binding.tvNamaObatDetail.text = obat.namaObat
            binding.tvJenisObatDetail.text = "Jenis: ${obat.jenisObat}"
            binding.tvStockDetail.text = "Stock: ${obat.stock}"

            obat.supplier?.let { supplier ->
                binding.tvSupplierDetail.text = "Supplier: ${supplier.namaSupplier}"
                binding.tvEmailSupplier.text = "Email: ${supplier.email}"
                binding.tvNomorSupplier.text = "Nomor: ${supplier.nomor}"
            }

            if (obat.gambarUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(obat.gambarUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.imgObatDetail)
            }
        }
    }

    private fun updateStock() {
        val obat = currentObat ?: return

        val quantityStr = binding.edtQuantity.text.toString().trim()

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Masukkan jumlah quantity", Toast.LENGTH_SHORT).show()
            return
        }

        val qty = quantityStr.toInt()
        val newStock = obat.stock + qty

        if (newStock >= 0) {
            viewModel.updateStock(obat.idObat, newStock)
            Toast.makeText(this, "Stock berhasil diupdate", Toast.LENGTH_SHORT).show()
            loadObatDetail(obat.idObat)
            quantity = 0
            binding.edtQuantity.setText("0")
        } else {
            Toast.makeText(this, "Stock tidak boleh negatif", Toast.LENGTH_SHORT).show()
        }
    }
}
