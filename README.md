# 🏥 Aplikasi Inventory Obat

**Inventory Obat** adalah aplikasi Android berbasis Java yang dirancang untuk memudahkan manajemen stok obat di apotek. Aplikasi ini memungkinkan administrator untuk mengelola data obat, memantau stok, mengelola supplier, dan melakukan operasi CRUD (Create, Read, Update, Delete) secara *real-time* melalui REST API dan autentikasi Firebase.

## ✨ Fitur Utama

* **Autentikasi Pengguna**: Login dan Registrasi aman menggunakan **Firebase Authentication**.
* **Dashboard Interaktif**: Menampilkan daftar obat dalam format Grid/List.
* **Kategorisasi Obat**: Filter obat berdasarkan jenis (Tablet, Sirup, Kapsul, Salep, dll) menggunakan Tab Layout.
* **Pencarian**: Fitur pencarian obat secara *real-time* berdasarkan nama.
* **Manajemen Stok**:
    * Melihat detail informasi obat.
    * Menambah dan mengurangi stok obat dengan cepat.
* **Manajemen Produk (CRUD)**:
    * Tambah obat baru beserta gambar (Upload Image).
    * Edit data obat.
    * Hapus obat.
* **Integrasi Supplier**: Menghubungkan obat dengan data supplier yang tersedia.

## 🛠️ Tech Stack & Library

Proyek ini dibangun menggunakan teknologi dan *library* berikut:

* **Bahasa Pemrograman**: Java (Utama) & Kotlin (Konfigurasi Build)
* **Arsitektur**: MVVM (Model-View-ViewModel)
* **UI Components**:
    * Material Design Components
    * RecyclerView & CardView
    * ViewPager & TabLayout
    * ConstraintLayout
* **Networking**:
    * **Retrofit 2**: Untuk komunikasi dengan REST API.
    * **OkHttp & Logging Interceptor**: Untuk debugging request jaringan.
    * **Gson**: Konversi JSON ke Java Object.
* **Backend & Auth**:
    * **Firebase Authentication**: Manajemen user session.
    * **Custom REST API**: Backend server untuk data obat dan supplier.
* **Image Loading**:
    * **Glide**: Menampilkan gambar dari URL secara efisien.
* **View Binding**: Menggantikan `findViewById` untuk keamanan tipe data view.

## ⚙️ Konfigurasi API

Aplikasi ini terhubung ke backend server yang didefinisikan dalam `ApiConfig.java`.

* **Base URL**: `https://apotek.astrantia.site/api/`

Pastikan server backend berjalan dan dapat diakses agar aplikasi berfungsi dengan baik. Endpoint utama meliputi:
* `GET /v1/obats` - Mengambil semua data obat.
* `POST /v1/obats` - Menambah obat baru (Multipart).
* `PUT /v1/obats/{id}` - Update data obat.
* `DELETE /v1/obats/{id}` - Menghapus obat.

## 🚀 Cara Menjalankan Project

1.  **Prasyarat**:
    * Android Studio Ladybug atau lebih baru (mendukung Gradle 8.13).
    * JDK 21 (sesuai konfigurasi `.idea/misc.xml`).
    * Koneksi Internet (untuk Gradle sync dan akses API).

2.  **Clone Repository**:
    ```bash
    git clone [https://github.com/username/inventory-obat.git](https://github.com/username/inventory-obat.git)
    ```

3.  **Setup Firebase**:
    * Pastikan file `google-services.json` yang valid sudah ada di dalam folder `app/`. File ini berisi konfigurasi untuk menghubungkan aplikasi ke project Firebase Anda.

4.  **Build & Run**:
    * Buka project di Android Studio.
    * Biarkan Gradle melakukan sinkronisasi (`Sync Project with Gradle Files`).
    * Jalankan aplikasi pada Emulator atau Perangkat Fisik.

## 📱 Permissions

Aplikasi membutuhkan izin berikut yang diatur dalam `AndroidManifest.xml` dan `PermissionHelper.java`:
* `INTERNET`: Untuk akses API dan Firebase.
* `READ_EXTERNAL_STORAGE` / `READ_MEDIA_IMAGES`: Untuk memilih gambar dari galeri saat menambah/edit obat (disesuaikan dengan versi Android/SDK 33+).

## 👤 Author
1. Lila Vimala
2. Cahya Nabila Mannassai
3. Syavira Humayra
4. Viska Inela
5. Amelia Wulandari
6. Aditya Zaldy
