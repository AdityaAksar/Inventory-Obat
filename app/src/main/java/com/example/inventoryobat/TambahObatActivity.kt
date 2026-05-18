package com.example.inventoryobat

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inventoryobat.databinding.ActivityTambahObatBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class TambahObatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahObatBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var apiService: ApiService
    private var selectedImageUri: Uri? = null
    private var supplierList: List<Supplier> = emptyList()
    private var selectedSupplierId = 0
    private var isEdit = false
    private var editObatId = -1

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahObatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = MainViewModel()
        apiService = ApiConfig.getApiService()

        isEdit = intent.getBooleanExtra("edit_mode", false)
        if (isEdit) {
            editObatId = intent.getIntExtra("obat_id", -1)
            binding.btnSimpan.text = "UPDATE OBAT"
        }

        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this)
        }

        setupJenisObatSpinner()
        setupSupplierSpinner()

        if (isEdit) {
            Handler(Looper.getMainLooper()).postDelayed({ loadDataForEdit() }, 200)
        }

        binding.btnPilihGambar.setOnClickListener { openImagePicker() }
        binding.btnSimpan.setOnClickListener { simpanObat() }
    }

    private fun setupJenisObatSpinner() {
        val jenisObatList = JenisObat.entries.map { it.displayName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisObatList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJenisObat.adapter = adapter
    }

    private fun setupSupplierSpinner() {
        viewModel.loadAllSuppliers()
        viewModel.supplierList.observe(this) { suppliers ->
            supplierList = suppliers
            val names = suppliers.map { it.namaSupplier }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSupplier.adapter = adapter

            binding.spinnerSupplier.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position in supplierList.indices) {
                        selectedSupplierId = supplierList[position].idSupplier
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun loadDataForEdit() {
        if (editObatId < 0) return

        viewModel.getObatById(editObatId).observe(this) { obat ->
            obat ?: return@observe

            binding.edtNamaObat.setText(obat.namaObat)
            binding.edtStock.setText(obat.stock.toString())

            val spinnerAdapter = binding.spinnerJenisObat.adapter as? ArrayAdapter<String>
            spinnerAdapter?.let {
                val position = it.getPosition(obat.jenisObat)
                binding.spinnerJenisObat.setSelection(if (position >= 0) position else 0)
            }

            for (i in supplierList.indices) {
                if (supplierList[i].idSupplier == obat.idSupplier) {
                    binding.spinnerSupplier.setSelection(i)
                    selectedSupplierId = obat.idSupplier
                    break
                }
            }

            if (obat.gambarUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(obat.gambarUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.imgPreview)
            }
        }
    }

    private fun openImagePicker() {
        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this)
            Toast.makeText(this, "Izin akses media diperlukan", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Izin diberikan", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Use Activity Result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            data.data?.let { uri ->
                selectedImageUri = uri
                try {
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (_: Exception) { }
                binding.imgPreview.setImageURI(uri)
            }
        }
    }

    private fun simpanObat() {
        val nama = binding.edtNamaObat.text.toString().trim()
        val stockStr = binding.edtStock.text.toString().trim()

        if (nama.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            return
        }

        val stock = stockStr.toInt()
        val jenis = binding.spinnerJenisObat.selectedItem as String
        val textPlain = "text/plain".toMediaTypeOrNull()

        if (isEdit) {
            val uri = selectedImageUri
            if (uri != null) {
                editWithImage(nama, jenis, stock, uri, textPlain)
            } else {
                editWithoutImage(nama, jenis, stock)
            }
        } else {
            createObat(nama, jenis, stock, textPlain)
        }
    }

    private fun editWithImage(nama: String, jenis: String, stock: Int, uri: Uri, textPlain: okhttp3.MediaType?) {
        val file = uriToFile(uri) ?: return
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val gambarPart = MultipartBody.Part.createFormData("gambar", file.name, requestBody)

        apiService.updateObatWithImage(
            editObatId,
            nama.toRequestBody(textPlain),
            jenis.toRequestBody(textPlain),
            stock.toString().toRequestBody(textPlain),
            selectedSupplierId.toString().toRequestBody(textPlain),
            gambarPart,
            "PUT".toRequestBody(textPlain)
        ).enqueue(object : Callback<ApiResponse<Obat>> {
            override fun onResponse(call: Call<ApiResponse<Obat>>, response: Response<ApiResponse<Obat>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@TambahObatActivity, "Update (Img) Berhasil!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@TambahObatActivity, "Gagal Update: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ApiResponse<Obat>>, t: Throwable) {
                Toast.makeText(this@TambahObatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editWithoutImage(nama: String, jenis: String, stock: Int) {
        apiService.updateObatNoImage(editObatId, nama, jenis, stock, selectedSupplierId)
            .enqueue(object : Callback<ApiResponse<Obat>> {
                override fun onResponse(call: Call<ApiResponse<Obat>>, response: Response<ApiResponse<Obat>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TambahObatActivity, "Update Data Berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@TambahObatActivity, "Gagal Update: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ApiResponse<Obat>>, t: Throwable) {
                    Toast.makeText(this@TambahObatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun createObat(nama: String, jenis: String, stock: Int, textPlain: okhttp3.MediaType?) {
        val namaBody = nama.toRequestBody(textPlain)
        val jenisBody = jenis.toRequestBody(textPlain)
        val stockBody = stock.toString().toRequestBody(textPlain)
        val supplierBody = selectedSupplierId.toString().toRequestBody(textPlain)

        var gambarPart: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            uriToFile(uri)?.let { file ->
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                gambarPart = MultipartBody.Part.createFormData("gambar", file.name, requestBody)
            }
        }

        val part = gambarPart
        if (part == null) {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.createObat(namaBody, jenisBody, stockBody, supplierBody, part)
            .enqueue(object : Callback<ApiResponse<Obat>> {
                override fun onResponse(call: Call<ApiResponse<Obat>>, response: Response<ApiResponse<Obat>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TambahObatActivity, "Obat berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@TambahObatActivity, "Gagal tambah obat: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ApiResponse<Obat>>, t: Throwable) {
                    Toast.makeText(this@TambahObatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { outputStream ->
                inputStream.use { input -> input.copyTo(outputStream) }
            }
            file
        } catch (_: Exception) {
            null
        }
    }
}
