package com.example.inventoryobat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val apiService: ApiService = ApiConfig.getApiService()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _obatList = MutableLiveData<List<Obat>>()
    val obatList: LiveData<List<Obat>> get() = _obatList

    private val _supplierList = MutableLiveData<List<Supplier>>()
    val supplierList: LiveData<List<Supplier>> get() = _supplierList

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> get() = _loginError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun login(email: String, password: String) {
        _isLoading.value = true
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _loginStatus.value = true
                } else {
                    _loginStatus.value = false
                    _loginError.value = task.exception?.message ?: "Login Gagal"
                }
            }
    }

    fun isUserLoggedIn(): Boolean = mAuth.currentUser != null

    fun logout() {
        mAuth.signOut()
    }

    fun loadAllObat() {
        _isLoading.value = true
        apiService.getAllObats().enqueue(object : Callback<ApiResponse<List<Obat>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Obat>>>,
                response: Response<ApiResponse<List<Obat>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _obatList.value = response.body()?.data
                } else {
                    _errorMessage.value = "Gagal memuat data obat"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Obat>>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }

    fun loadObatByJenis(jenis: String) {
        _isLoading.value = true
        apiService.getObatByJenis(jenis).enqueue(object : Callback<ApiResponse<List<Obat>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Obat>>>,
                response: Response<ApiResponse<List<Obat>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _obatList.value = response.body()?.data
                } else {
                    _errorMessage.value = "Gagal memuat data obat"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Obat>>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }

    fun getObatById(id: Int): LiveData<Obat> {
        val result = MutableLiveData<Obat>()
        apiService.getObatById(id).enqueue(object : Callback<ApiResponse<Obat>> {
            override fun onResponse(
                call: Call<ApiResponse<Obat>>,
                response: Response<ApiResponse<Obat>>
            ) {
                if (response.isSuccessful) {
                    result.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<ApiResponse<Obat>>, t: Throwable) {
                _errorMessage.value = "Error: ${t.message}"
            }
        })
        return result
    }

    fun deleteObat(id: Int) {
        apiService.deleteObat(id).enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    loadAllObat()
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }

    fun updateStock(id: Int, newStock: Int) {
        apiService.updateStock(id, UpdateStockRequest(newStock))
            .enqueue(object : Callback<ApiResponse<Obat>> {
                override fun onResponse(
                    call: Call<ApiResponse<Obat>>,
                    response: Response<ApiResponse<Obat>>
                ) {
                    if (response.isSuccessful) {
                        loadAllObat()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Obat>>, t: Throwable) {
                    _errorMessage.value = "Error: ${t.message}"
                }
            })
    }

    fun loadAllSuppliers() {
        apiService.getAllSuppliers().enqueue(object : Callback<ApiResponse<List<Supplier>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Supplier>>>,
                response: Response<ApiResponse<List<Supplier>>>
            ) {
                if (response.isSuccessful) {
                    _supplierList.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Supplier>>>, t: Throwable) {
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}
