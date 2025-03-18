package com.example.mealscape;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MealsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MealscapeDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_FAVORITES = "Favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_RECIPE = "recipe";

    public MealsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public boolean isFavoriteExists(String recipe) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM favorites WHERE recipe = ?", new String[]{recipe});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_RECIPE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public long addFavorite(String name, String recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_RECIPE, recipe);
        return db.insert(TABLE_FAVORITES, null, values);
    }

    public List<String> getFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null);
        List<String> favorites = new ArrayList<>();
        while (cursor.moveToNext()) {
             String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
             String recipe = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPE));
            favorites.add(name + ": " + recipe);
        }
        cursor.close();
        return favorites;
    }
}
