package com.example.rotaricartedtlfn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PhoneBook.db";
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_CONTACTS = "Contacts";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT, Password TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Phone TEXT, Email TEXT, Image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Inserare utilizator
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", username);
        contentValues.put("Password", password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Verificare logare utilizator
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE Username=? AND Password=?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    // Operațiuni CRUD pentru contacte
    public boolean insertContact(String name, String phone, String email, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Phone", phone);
        contentValues.put("Email", email);
        contentValues.put("Image", image);
        long result = db.insert(TABLE_CONTACTS, null, contentValues);
        return result != -1;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4)
                );
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contacts;
    }

    public boolean updateContact(int id, String name, String phone, String email, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Phone", phone);
        contentValues.put("Email", email);
        contentValues.put("Image", image);
        int result = db.update(TABLE_CONTACTS, contentValues, "ID = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CONTACTS, "ID = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}