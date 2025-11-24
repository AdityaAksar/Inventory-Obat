package com.example.inventoryobat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private final ApiService apiService;
    private final MutableLiveData<List<Obat>> obatListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Supplier>> supplierListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MainViewModel() {
        apiService = ApiConfig.getApiService();
    }

    // ========== LOGIN ==========
    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public void login(String username, String password) {
        // Untuk sekarang, validasi lokal (bisa dikembangkan ke server)
        if ("admin".equals(username) && "admin".equals(password)) {
            loginStatus.setValue(true);
        } else {
            loginStatus.setValue(false);
        }
    }

    // ========== OBAT ==========
    public LiveData<List<Obat>> getObatList() {
        return obatListLiveData;
    }

    public void loadAllObat() {
        isLoading.setValue(true);
        apiService.getAllObats().enqueue(new Callback<ApiResponse<List<Obat>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Obat>>> call, Response<ApiResponse<List<Obat>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    obatListLiveData.setValue(response.body().getData());
                } else {
                    errorMessage.setValue("Gagal memuat data obat");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Obat>>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void loadObatByJenis(String jenis) {
        isLoading.setValue(true);
        apiService.getObatByJenis(jenis).enqueue(new Callback<ApiResponse<List<Obat>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Obat>>> call, Response<ApiResponse<List<Obat>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    obatListLiveData.setValue(response.body().getData());
                } else {
                    errorMessage.setValue("Gagal memuat data obat");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Obat>>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public LiveData<Obat> getObatById(int id) {
        MutableLiveData<Obat> result = new MutableLiveData<>();
        apiService.getObatById(id).enqueue(new Callback<ApiResponse<Obat>>() {
            @Override
            public void onResponse(Call<ApiResponse<Obat>> call, Response<ApiResponse<Obat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Obat>> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
        return result;
    }

    public void deleteObat(int id) {
        apiService.deleteObat(id).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    loadAllObat();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void updateStock(int id, int newStock) {
        apiService.updateStock(id, new UpdateStockRequest(newStock))
                .enqueue(new Callback<ApiResponse<Obat>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Obat>> call, Response<ApiResponse<Obat>> response) {
                        if (response.isSuccessful()) {
                            loadAllObat();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Obat>> call, Throwable t) {
                        errorMessage.setValue("Error: " + t.getMessage());
                    }
                });
    }

    // ========== SUPPLIER ==========
    public LiveData<List<Supplier>> getSupplierList() {
        return supplierListLiveData;
    }

    public void loadAllSuppliers() {
        apiService.getAllSuppliers().enqueue(new Callback<ApiResponse<List<Supplier>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Supplier>>> call, Response<ApiResponse<List<Supplier>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    supplierListLiveData.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Supplier>>> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    // ========== LOADING & ERROR ==========
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
