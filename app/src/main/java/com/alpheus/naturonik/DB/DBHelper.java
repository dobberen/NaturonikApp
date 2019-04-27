package com.alpheus.naturonik.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "naturonik.db";
    private static final int DATABASE_VERSION = 1;

    private final static String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + DBContract.ProductEntry.TABLE_NAME + " (" +
            DBContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DBContract.ProductEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
            DBContract.ProductEntry.COLUMN_SORT + " TEXT NOT NULL, " +
            DBContract.ProductEntry.COLUMN_IMG + " TEXT NOT NULL, " +
            DBContract.ProductEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            DBContract.ProductEntry.COLUMN_TYPE_PACK + " TEXT NOT NULL, " +
            DBContract.ProductEntry.COLUMN_PRICE + " TEXT NOT NULL " + " );";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        DBHelper db = new DBHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        Log.d(TAG, "База данных успешно создана");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ProductEntry.TABLE_NAME);
        onCreate(db);
    }

    void addJson(JSONObject json) throws JSONException {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.ProductEntry._ID, json.getString("id"));
        values.put(DBContract.ProductEntry.COLUMN_COUNTRY, json.getString("countrys_name"));
        values.put(DBContract.ProductEntry.COLUMN_SORT, json.getString("sorts_name"));
        values.put(DBContract.ProductEntry.COLUMN_IMG, json.getString("img"));
        values.put(DBContract.ProductEntry.COLUMN_DESCRIPTION, json.getString("description"));
        values.put(DBContract.ProductEntry.COLUMN_TYPE_PACK, json.getString("type_pack"));
        values.put(DBContract.ProductEntry.COLUMN_PRICE, json.getString("price"));

        db.insert(DBContract.ProductEntry.TABLE_NAME, null, values);
        db.close();


    }
}
