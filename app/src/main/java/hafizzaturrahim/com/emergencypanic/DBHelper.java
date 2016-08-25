package hafizzaturrahim.com.emergencypanic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Hafizh on 21/05/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db = this.getWritableDatabase();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "db_ep";

//    private static final String CREATE_USER =
//            "CREATE TABLE USER(" +
//                    "ID_USER INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "NAMA_USER VARCHAR(30)," +
//                    "NOMOR_DARURAT VARCHAR(15))";

    private static final String CREATE_KATEGORI =
            "CREATE TABLE KATEGORI(" +
                    "ID_KATEGORI INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAMA_KATEGORI VARCHAR(30))";

    private static final String CREATE_TEMPAT =
            "CREATE TABLE TEMPAT(" +
                    "ID_TEMPAT INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ID_KATEGORI REFERENCES KATEGORI(ID_KATEGORI)," +
                    "NAMA_TEMPAT VARCHAR(100)," +
                    "ALAMAT TEXT," +
                    "NO_TELP VARCHAR(15)," +
                    "LATITUDE VARCHAR(30)," +
                    "LONGITUDE VARCHAR(30)," +
                    "PLACE_ID TEXT)";
    private static final String CREATE_TEMPAT_TEMP =
            "CREATE TABLE TEMPAT_TEMP(" +
                    "ID_TEMPAT TEXT PRIMARY KEY NOT NULL," +
                    "ID_KATEGORI REFERENCES KATEGORI(ID_KATEGORI)," +
                    "NAMA_TEMPAT VARCHAR(100)," +
                    "ALAMAT TEXT," +
                    "NO_TELP VARCHAR(15)," +
                    "LATITUDE VARCHAR(30)," +
                    "LONGITUDE VARCHAR(30)," +
                    "UNIQUE(ID_TEMPAT) ON CONFLICT REPLACE)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
//        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_KATEGORI);
        db.execSQL(CREATE_TEMPAT);
        db.execSQL(CREATE_TEMPAT_TEMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS KATEGORI");
        db.execSQL("DROP TABLE IF EXISTS TEMPAT");
        db.execSQL("DROP TABLE IF EXISTS TEMPAT_TEMP");
        // create new tables
        onCreate(db);
    }

    public long insertKategori(int id, String nama_kategori) {
        ContentValues values = new ContentValues();
        values.put("ID_KATEGORI", id);
        values.put("NAMA_KATEGORI", nama_kategori);

        // insert row
        long insert = db.insert("KATEGORI", null, values);
        // Closing database connection
        return insert;
    }

    public int countKategori() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM KATEGORI";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return 0;
        } else {
            return 1;
        }
    }

//    public void createUser(String nama, String no_telp){
//        ContentValues values = new ContentValues();
//        values.put("NAMA_USER", nama);
//        values.put("NOMOR_DARURAT", no_telp);
//
//        // insert row
//        db.insert("USER", null, values);
//        // Closing database connection
//        db.close();
//    }
//
//    public void updateUser(String no_telp){
//        ContentValues values = new ContentValues();
//        values.put("NOMOR_DARURAT",no_telp);
//        db.update("USER",values,"1 = ?", new String[]{"1"});
//    }

    public long insertTempat(Tempat location) {
        ContentValues values = new ContentValues();
        ArrayList<Tempat> control = getAllTempat(location.getId_kategori());
        for (int i = 0; i < control.size(); i++) {
            if (location.getPlace_id() != control.get(i).getPlace_id()) {
                values.put("ID_KATEGORI", location.getId_kategori());
                values.put("NAMA_TEMPAT", location.getNama_tempat());
                values.put("ALAMAT", location.getAlamat());
                values.put("NO_TELP", location.getNo_telp());
                values.put("LATITUDE", location.getLatitude());
                values.put("LONGITUDE", location.getLongitude());
                values.put("PLACE_ID", location.getPlace_id());

                Log.v("Inserting Tempat ", "sukses " + location.getPlace_id());
            }
        }

        // insert row
        long insert = db.insert("TEMPAT", null, values);

        return insert;
    }

    public long insertTempat(int id_kategori, String nama_tempat, String alamat, String no_telp, double latitude, double longitude, String place_id) {
        ContentValues values = new ContentValues();
        ArrayList<Tempat> control = getAllTempat(id_kategori);

        long insert = 0;
        boolean isExist = true;
        if (control.isEmpty()) {
            Log.v("control", "kosong " +id_kategori);
//                values.put("ID_KATEGORI", id_kategori);
//                values.put("NAMA_TEMPAT", nama_tempat);
//                values.put("ALAMAT", alamat);
//                values.put("NO_TELP", no_telp);
//                values.put("LATITUDE", latitude);
//                values.put("LONGITUDE", longitude);
//                values.put("PLACE_ID", place_id);
//                // insert row
//                insert = db.insert("TEMPAT", null, values);
            isExist = false;
            Log.v("InsertTempat kosong ", "kosong sukses " + place_id);
        }else{
            for (int i = 0; i < control.size(); i++) {
                //if there is no data in table Tempat
                if(id_kategori < 4){
                    //check if data is exist
                    if (place_id.contains(control.get(i).getPlace_id())) {
                        Log.v("insertTempat ", place_id + " + sudah ada " + control.get(i).getPlace_id());
                        isExist = true;
                        break;
                    } else {
                        isExist = false;
                        Log.v("insertTempat ", control.get(i).getPlace_id() + " belum ada, siap diinsert" + place_id);
                    }
                }else{
                    isExist = false;
                }
            }

        }

        if (!isExist) {
            values.put("ID_KATEGORI", id_kategori);
            values.put("NAMA_TEMPAT", nama_tempat);
            values.put("ALAMAT", alamat);
            values.put("NO_TELP", no_telp);
            values.put("LATITUDE", latitude);
            values.put("LONGITUDE", longitude);
            values.put("PLACE_ID", place_id);
            // insert row
            insert = db.insert("TEMPAT", null, values);
            Log.v("InsertTempat ins sukses", id_kategori + " sukses " + nama_tempat+" , " +place_id);
        }
        return insert;
    }

    public long insertTempat_TEMP(Tempat location) {
        ContentValues values = new ContentValues();
        values.put("ID_TEMPAT", location.getPlace_id());
        values.put("ID_KATEGORI", location.getId_kategori());
        values.put("NAMA_TEMPAT", location.getNama_tempat());
        values.put("ALAMAT", location.getAlamat());
        values.put("NO_TELP", location.getNo_telp());
        values.put("LATITUDE", location.getLatitude());
        values.put("LONGITUDE", location.getLongitude());

        // insert row
        long insert = db.insert("TEMPAT_TEMP", null, values);
        // Closing database connection
        Log.v("Inserting temp", "sukses");
        return insert;
    }

    public void insertTempat_TEMP(String id_tempat, int id_kategori, String nama_tempat, String alamat, String no_telp, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put("ID_TEMPAT", id_tempat);
        values.put("ID_KATEGORI", id_kategori);
        values.put("NAMA_TEMPAT", nama_tempat);
        values.put("ALAMAT", alamat);
        values.put("NO_TELP", no_telp);
        values.put("LATITUDE", latitude);
        values.put("LONGITUDE", longitude);

        // insert row
        db.insert("TEMPAT_TEMP", null, values);
        // Closing database connection
        db.close();
    }

    public int countTemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM TEMPAT_TEMP";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int hasil = cursor.getCount();
        cursor.close();
        return hasil;
    }

    public int countTempat() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM TEMPAT";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int hasil = cursor.getCount();
        cursor.close();
        return hasil;
    }

    public int countTempat(int id_kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM TEMPAT WHERE ID_KATEGORI = "+id_kategori;

        Cursor cursor = db.rawQuery(selectQuery, null);
        int hasil = cursor.getCount();
        cursor.close();
        return hasil;
    }

    public ArrayList<Tempat> getAllTemp() {
        ArrayList<Tempat> place = new ArrayList<>();
        String selectQuery = "SELECT * FROM TEMPAT_TEMP";

        Log.e("SELECT TEMPAT_TEMP", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tempat tempat = new Tempat();
                tempat.setPlace_id(c.getString(c.getColumnIndex("ID_TEMPAT")));
                tempat.setNama_tempat(c.getString(c.getColumnIndex("NAMA_TEMPAT")));
                tempat.setAlamat(c.getString(c.getColumnIndex("ALAMAT")));
                tempat.setLatitude(c.getDouble(c.getColumnIndex("LATITUDE")));
                tempat.setLongitude(c.getDouble(c.getColumnIndex("LONGITUDE")));
                place.add(tempat);
            } while (c.moveToNext());
        }
        c.close();
        return place;
    }

    public ArrayList<Tempat> getAllTempat(int id_category) {
        ArrayList<Tempat> place = new ArrayList<>();
        String selectQuery = "SELECT * FROM TEMPAT WHERE ID_KATEGORI = " + id_category;

        Log.e("SELECT TEMPAT", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tempat tempat = new Tempat();
                tempat.setId_kategori(c.getInt(c.getColumnIndex("ID_KATEGORI")));
                tempat.setNama_tempat(c.getString(c.getColumnIndex("NAMA_TEMPAT")));
                tempat.setAlamat(c.getString(c.getColumnIndex("ALAMAT")));
                tempat.setNo_telp(c.getString(c.getColumnIndex("NO_TELP")));
                tempat.setLatitude(c.getDouble(c.getColumnIndex("LATITUDE")));
                tempat.setLongitude(c.getDouble(c.getColumnIndex("LONGITUDE")));
                tempat.setPlace_id(c.getString(c.getColumnIndex("PLACE_ID")));
                place.add(tempat);
            } while (c.moveToNext());
        }

        c.close();
        return place;
    }

    public void deleteTemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Delete Temp", "Deleting temporary table...");
        db.delete("TEMPAT_TEMP", null, null);
    }

    public void deleteTempat() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Delete Temp", "Deleting table tempat...");
        db.delete("TEMPAT", null, null);
    }


}
