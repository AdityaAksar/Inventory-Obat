package com.example.inventoryobat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory_apotek.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String TABLE_SUPPLIER = "supplier";
    private static final String TABLE_OBAT = "obat";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Insert default admin user
        ContentValues adminValues = new ContentValues();
        adminValues.put("username", "admin");
        adminValues.put("password", "admin");
        db.insert(TABLE_USER, null, adminValues);

        String CREATE_SUPPLIER_TABLE = "CREATE TABLE " + TABLE_SUPPLIER + "("
                + "id_supplier INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama_supplier TEXT NOT NULL,"
                + "nomor TEXT NOT NULL,"
                + "email TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_SUPPLIER_TABLE);

        String CREATE_OBAT_TABLE = "CREATE TABLE " + TABLE_OBAT + "("
                + "id_obat INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama_obat TEXT NOT NULL,"
                + "jenis_obat TEXT NOT NULL,"
                + "stock INTEGER NOT NULL,"
                + "gambar TEXT,"
                + "id_supplier INTEGER,"
                + "FOREIGN KEY(id_supplier) REFERENCES "
                + TABLE_SUPPLIER + "(id_supplier)"
                + ")";
        db.execSQL(CREATE_OBAT_TABLE);

        // Insert default suppliers
        insertDefaultSuppliers(db);
    }

    private void insertDefaultSuppliers(SQLiteDatabase db) {
        String[][] suppliers = {
                {"PT Penta Valent", "021-5673891", "pharmasi@pv.co.id"},
                {"PT Perdhaki", "021-3900601, 3909245", "mensana@cbn.net.id"},
                {"PT Prambanan Kencana", "021-3806464", "prbkcnod"},
                {"PT Sawah Besar Farma", "021-5601274", "sbfmrkt0@cbn.net.id"},
                {"PT Signa Husada", "021-5446344, 5446345", "signa@dnet.net.id"},
                {"PT Tiara Kencana", "021-7987862", "tkencana@cbn.net.id"},
                {"PT Tigaka Distrindo Perkasa", "021-3508981", "pttdp@cbn.net.id"},
                {"PT Transfarma Medica Indah", "021-7697323-7", "tfmi@cbn.net.id"},
                {"PT Wigo Distribusi Farmasi", "021-7258010", "sales@wigo-df.com"},
                {"PT Elo Karsa Utama", "021-739 2856, 720 1893", "eku@cbn.net.id, agussantoso@elokarsa.com"}
        };

        for (String[] supplier : suppliers) {
            ContentValues values = new ContentValues();
            values.put("nama_supplier", supplier[0]);
            values.put("nomor", supplier[1]);
            values.put("email", supplier[2]);
            db.insert(TABLE_SUPPLIER, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);
        onCreate(db);
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert(TABLE_USER, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE username=? AND password=?",
                new String[]{username, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public long insertSupplier(Supplier supplier) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama_supplier", supplier.getNamaSupplier());
        values.put("nomor", supplier.getNomor());
        values.put("email", supplier.getEmail());

        return db.insert(TABLE_SUPPLIER, null, values);
    }

    public ArrayList<Supplier> getAllSuppliers() {
        ArrayList<Supplier> supplierList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUPPLIER, null);

        if (cursor.moveToFirst()) {
            do {
                Supplier supplier = new Supplier();
                supplier.setIdSupplier(cursor.getInt(0));
                supplier.setNamaSupplier(cursor.getString(1));
                supplier.setNomor(cursor.getString(2));
                supplier.setEmail(cursor.getString(3));
                supplierList.add(supplier);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return supplierList;
    }

    public long insertObat(Obat obat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama_obat", obat.getNamaObat());
        values.put("jenis_obat", obat.getJenisObat().name());
        values.put("stock", obat.getStock());
        values.put("gambar", obat.getGambar());
        values.put("id_supplier", obat.getIdSupplier());

        return db.insert(TABLE_OBAT, null, values);
    }

    public ArrayList<Obat> getAllObat() {
        ArrayList<Obat> obatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, s.nama_supplier, s.nomor, s.email " +
                "FROM " + TABLE_OBAT + " o " +
                "LEFT JOIN " + TABLE_SUPPLIER + " s " +
                "ON o.id_supplier = s.id_supplier";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Obat obat = new Obat();
                obat.setIdObat(cursor.getInt(0));
                obat.setNamaObat(cursor.getString(1));
                obat.setJenisObat(JenisObat.valueOf(cursor.getString(2)));
                obat.setStock(cursor.getInt(3));
                obat.setGambar(cursor.getString(4));
                obat.setIdSupplier(cursor.getInt(5));

                if (!cursor.isNull(6)) {
                    Supplier supplier = new Supplier();
                    supplier.setIdSupplier(cursor.getInt(5));
                    supplier.setNamaSupplier(cursor.getString(6));
                    supplier.setNomor(cursor.getString(7));
                    supplier.setEmail(cursor.getString(8));
                    obat.setSupplier(supplier);
                }

                obatList.add(obat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return obatList;
    }

    public ArrayList<Obat> getObatByJenis(String jenisObat) {
        ArrayList<Obat> obatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, s.nama_supplier, s.nomor, s.email " +
                "FROM " + TABLE_OBAT + " o " +
                "LEFT JOIN " + TABLE_SUPPLIER + " s " +
                "ON o.id_supplier = s.id_supplier " +
                "WHERE o.jenis_obat = ?";

        Cursor cursor = db.rawQuery(query, new String[]{jenisObat});

        if (cursor.moveToFirst()) {
            do {
                Obat obat = new Obat();
                obat.setIdObat(cursor.getInt(0));
                obat.setNamaObat(cursor.getString(1));
                obat.setJenisObat(JenisObat.valueOf(cursor.getString(2)));
                obat.setStock(cursor.getInt(3));
                obat.setGambar(cursor.getString(4));
                obat.setIdSupplier(cursor.getInt(5));

                if (!cursor.isNull(6)) {
                    Supplier supplier = new Supplier();
                    supplier.setIdSupplier(cursor.getInt(5));
                    supplier.setNamaSupplier(cursor.getString(6));
                    supplier.setNomor(cursor.getString(7));
                    supplier.setEmail(cursor.getString(8));
                    obat.setSupplier(supplier);
                }

                obatList.add(obat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return obatList;
    }

    public boolean updateStock(int idObat, int newStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stock", newStock);

        int result = db.update(TABLE_OBAT, values,
                "id_obat=?",
                new String[]{String.valueOf(idObat)});
        return result > 0;
    }

    public Obat getObatById(int idObat) {
        SQLiteDatabase db = this.getReadableDatabase();
        Obat obat = null;

        String query = "SELECT o.*, s.nama_supplier, s.nomor, s.email " +
                "FROM " + TABLE_OBAT + " o " +
                "LEFT JOIN " + TABLE_SUPPLIER + " s " +
                "ON o.id_supplier = s.id_supplier " +
                "WHERE o.id_obat = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idObat)});

        if (cursor.moveToFirst()) {
            obat = new Obat();
            obat.setIdObat(cursor.getInt(0));
            obat.setNamaObat(cursor.getString(1));
            obat.setJenisObat(JenisObat.valueOf(cursor.getString(2)));
            obat.setStock(cursor.getInt(3));
            obat.setGambar(cursor.getString(4));
            obat.setIdSupplier(cursor.getInt(5));

            if (!cursor.isNull(6)) {
                Supplier supplier = new Supplier();
                supplier.setIdSupplier(cursor.getInt(5));
                supplier.setNamaSupplier(cursor.getString(6));
                supplier.setNomor(cursor.getString(7));
                supplier.setEmail(cursor.getString(8));
                obat.setSupplier(supplier);
            }
        }
        cursor.close();
        return obat;
    }
    public boolean updateObat(Obat obat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama_obat", obat.getNamaObat());
        values.put("jenis_obat", obat.getJenisObat().name());
        values.put("stock", obat.getStock());
        values.put("gambar", obat.getGambar());
        values.put("id_supplier", obat.getIdSupplier());
        int result = db.update(TABLE_OBAT, values,
                "id_obat=?", new String[]{String.valueOf(obat.getIdObat())});
        return result > 0;
    }

    public boolean deleteObat(int idObat) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_OBAT,
                "id_obat=?", new String[]{String.valueOf(idObat)});
        return result > 0;
    }
}