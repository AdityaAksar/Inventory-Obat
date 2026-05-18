package com.example.inventoryobat

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("v1/obats")
    fun getAllObats(): Call<ApiResponse<List<Obat>>>

    @GET("v1/obats/{id}")
    fun getObatById(@Path("id") id: Int): Call<ApiResponse<Obat>>

    @GET("v1/obats/jenis/{jenis}")
    fun getObatByJenis(@Path("jenis") jenis: String): Call<ApiResponse<List<Obat>>>

    @Multipart
    @POST("v1/obats")
    fun createObat(
        @Part("nama_obat") nama: RequestBody,
        @Part("jenis_obat") jenis: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("id_supplier") idSupplier: RequestBody,
        @Part gambar: MultipartBody.Part
    ): Call<ApiResponse<Obat>>

    @Multipart
    @POST("v1/obats/{id}")
    fun updateObatWithImage(
        @Path("id") id: Int,
        @Part("nama_obat") nama: RequestBody,
        @Part("jenis_obat") jenis: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("id_supplier") idSupplier: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("_method") method: RequestBody
    ): Call<ApiResponse<Obat>>

    @FormUrlEncoded
    @PUT("v1/obats/{id}")
    fun updateObatNoImage(
        @Path("id") id: Int,
        @Field("nama_obat") nama: String,
        @Field("jenis_obat") jenis: String,
        @Field("stock") stock: Int,
        @Field("id_supplier") idSupplier: Int
    ): Call<ApiResponse<Obat>>

    @PUT("v1/obats/{id}/stock")
    fun updateStock(
        @Path("id") id: Int,
        @Body request: UpdateStockRequest
    ): Call<ApiResponse<Obat>>

    @DELETE("v1/obats/{id}")
    fun deleteObat(@Path("id") id: Int): Call<ApiResponse<String>>

    @GET("v1/suppliers")
    fun getAllSuppliers(): Call<ApiResponse<List<Supplier>>>

    @GET("v1/suppliers/{id}")
    fun getSupplierById(@Path("id") id: Int): Call<ApiResponse<Supplier>>

    @POST("v1/suppliers")
    fun createSupplier(@Body supplier: Supplier): Call<ApiResponse<Supplier>>

    @PUT("v1/suppliers/{id}")
    fun updateSupplier(@Path("id") id: Int, @Body supplier: Supplier): Call<ApiResponse<Supplier>>

    @DELETE("v1/suppliers/{id}")
    fun deleteSupplier(@Path("id") id: Int): Call<ApiResponse<String>>
}