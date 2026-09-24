package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "forum_db.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_UTILISATEURS = "utilisateurs";
    public static final String COL_ID = "id";
    public static final String COL_NOM = "nom";
    public static final String COL_AGE = "age";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_UTILISATEURS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NOM + " TEXT NOT NULL, "
            + COL_AGE + " INTEGER NOT NULL"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEURS);
        onCreate(db);
    }

    public boolean ajouterUtilisateur(String nom, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOM, nom);
        values.put(COL_AGE, age);

        long result = db.insert(TABLE_UTILISATEURS, null, values);
        return result != -1;
    }

    public List<Utilisateur> getTousLesUtilisateurs() {
        List<Utilisateur> liste = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_UTILISATEURS + " ORDER BY " + COL_ID + " ASC", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndexOrThrow(COL_ID);
                    int nomIndex = cursor.getColumnIndexOrThrow(COL_NOM);
                    int ageIndex = cursor.getColumnIndexOrThrow(COL_AGE);

                    int id = cursor.getInt(idIndex);
                    String nom = cursor.getString(nomIndex);
                    int age = cursor.getInt(ageIndex);

                    liste.add(new Utilisateur(id, nom, age));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return liste;
    }

    public boolean supprimerUtilisateur(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_UTILISATEURS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public void viderTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UTILISATEURS, null, null);
    }

    public int getNombreUtilisateurs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_UTILISATEURS, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }
}
