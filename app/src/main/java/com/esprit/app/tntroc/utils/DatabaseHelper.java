package com.esprit.app.tntroc.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.esprit.app.tntroc.Entity.Annonce;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "favoris_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Annonce.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Annonce.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertAnnonce(int idAnnonce, String titre, int prix, String img) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Annonce.COLUMN_ID, idAnnonce);
        values.put(Annonce.COLUMN_TITRE, titre);
        values.put(Annonce.COLUMN_PRIX, prix);
        values.put(Annonce.COLUMN_IMG, img);

        // insert row
        long id = db.insert(Annonce.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Annonce getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Annonce.TABLE_NAME,
                new String[]{Annonce.COLUMN_ID, Annonce.COLUMN_TITRE, Annonce.COLUMN_PRIX, Annonce.COLUMN_IMG},
                Annonce.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Annonce annonce = new Annonce(
                cursor.getInt(cursor.getColumnIndex(Annonce.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Annonce.COLUMN_TITRE)),
                cursor.getInt(cursor.getColumnIndex(Annonce.COLUMN_PRIX)),
                cursor.getString(cursor.getColumnIndex(Annonce.COLUMN_IMG)));

        // close the db connection
        cursor.close();

        return annonce;
    }

    public List<Annonce> getAllPosts() {
        List<Annonce> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Annonce.TABLE_NAME ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Annonce note = new Annonce();
                note.setId(cursor.getInt(cursor.getColumnIndex(Annonce.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Annonce.COLUMN_TITRE)));
                note.setPrix(cursor.getInt(cursor.getColumnIndex(Annonce.COLUMN_PRIX)));
                note.setImg(cursor.getString(cursor.getColumnIndex(Annonce.COLUMN_IMG)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void deleteNote(Annonce annonce) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Annonce.TABLE_NAME, Annonce.COLUMN_ID + " = ?",
                new String[]{String.valueOf(annonce.getId())});
        db.close();
    }
}
