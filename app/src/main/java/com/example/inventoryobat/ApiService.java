package com.example.inventoryobat;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // ========== OBAT ENDPOINTS ==========
    @GET("v1/obats")
    Call<ApiResponse<List<Obat>>> getAllObats();

    @GET("v1/obats/{id}")
    Call<ApiResponse<Obat>> getObatById(@Path("id") int id);

    @GET("v1/obats/jenis/{jenis}")
    Call<ApiResponse<List<Obat>>> getObatByJenis(@Path("jenis") String jenis);

    @Multipart
    @POST("v1/obats")
    Call<ApiResponse<Obat>> createObat(
            @Part("nama_obat") RequestBody nama,
            @Part("jenis_obat") RequestBody jenis,
            @Part("stock") RequestBody stock,
            @Part("id_supplier") RequestBody idSupplier,
            @Part MultipartBody.Part gambar
    );

    @Multipart
    @POST("v1/obats/{id}")
    Call<ApiResponse<Obat>> updateObatWithImage(
            @Path("id") int id,
            @Part("nama_obat") RequestBody nama,
            @Part("jenis_obat") RequestBody jenis,
            @Part("stock") RequestBody stock,
            @Part("id_supplier") RequestBody idSupplier,
            @Part MultipartBody.Part gambar,
            @Part("_method") RequestBody method
    );

    @FormUrlEncoded
    @PUT("v1/obats/{id}")
    Call<ApiResponse<Obat>> updateObatNoImage(
            @Path("id") int id,
            @Field("nama_obat") String nama,
            @Field("jenis_obat") String jenis,
            @Field("stock") int stock,
            @Field("id_supplier") int idSupplier
    );

    @PUT("v1/obats/{id}/stock")
    Call<ApiResponse<Obat>> updateStock(
            @Path("id") int id,
            @Body UpdateStockRequest request
    );

    @DELETE("v1/obats/{id}")
    Call<ApiResponse<String>> deleteObat(@Path("id") int id);

    // ========== SUPPLIER ENDPOINTS ==========
    @GET("v1/suppliers")
    Call<ApiResponse<List<Supplier>>> getAllSuppliers();

    @GET("v1/suppliers/{id}")
    Call<ApiResponse<Supplier>> getSupplierById(@Path("id") int id);

    @POST("v1/suppliers")
    Call<ApiResponse<Supplier>> createSupplier(@Body Supplier supplier);

    @PUT("v1/suppliers/{id}")
    Call<ApiResponse<Supplier>> updateSupplier(@Path("id") int id, @Body Supplier supplier);

    @DELETE("v1/suppliers/{id}")
    Call<ApiResponse<String>> deleteSupplier(@Path("id") int id);
}