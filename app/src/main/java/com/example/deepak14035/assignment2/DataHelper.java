package com.example.deepak14035.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DICTIONARY_TABLE_NAME = "dictionary";
    public static final String DATABASE_NAME = "DBA1";
    public static final String PLAYER_NAME = "NAME", PLAYER_SCORE="SCORE", CATEGORY="CATEGORY";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    PLAYER_NAME + " TEXT, " +
                    PLAYER_SCORE + " TEXT, "+CATEGORY+" TEXT);";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void clearData(SQLiteDatabase db){
        db.execSQL("delete from " + DICTIONARY_TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+DICTIONARY_TABLE_NAME+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return;
            }
            cursor.close();
        }
        db.execSQL(DICTIONARY_TABLE_CREATE);
        Log.e("asd", "table created");

    }

    public void updateTime(SQLiteDatabase db, String name, String score, String category){
        Cursor allRows = db.rawQuery("SELECT * FROM " + DICTIONARY_TABLE_NAME + " WHERE " + PLAYER_NAME + " = '" + name + "'", null);
        ContentValues content = new ContentValues();
        content.put(PLAYER_NAME, name);
        content.put(PLAYER_SCORE, score);
        content.put(CATEGORY, category);
        db.update(DICTIONARY_TABLE_NAME, content, "NAME='" + name + "' AND " + CATEGORY + " = '" + category + "'", null);
    }

    public boolean checkIfNameAlreadyExists(SQLiteDatabase db, String name, String category){
        String strQuery = String.format(
                "SELECT * FROM %s WHERE %s = '%s'", DICTIONARY_TABLE_NAME,
                PLAYER_NAME, name);
        Cursor allRows = db.rawQuery(strQuery, null);
        String tableString="";
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();

            do {
                for (String name1: columnNames) {
                    tableString += String.format("%s: %s\n", name1,
                            allRows.getString(allRows.getColumnIndex(name1)));
                    if(allRows.getString(allRows.getColumnIndex(name1)).contains(category)){
                        return true;
                    }
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        return false;
        /*Log.e("asd", tableString);
        if(allRows==null) Log.e("asd", "false");
        else Log.e("asd", "true");
        if(allRows==null) return false;
        return true;*/

    }

    public void insertValues(SQLiteDatabase db, String name, String score, String category){
        /*String insertDocument = "INSERT INTO " + DICTIONARY_TABLE_NAME + " (" + PLAYER_NAME + ", " + PLAYER_SCORE + ") VALUES ('";
        String insertDocument2 = insertDocument +name+"'"+", '"+score+"');";
        db.execSQL(insertDocument2);*/
        ContentValues content = new ContentValues();
        content.put(PLAYER_NAME, name);
        content.put(PLAYER_SCORE, score);
        content.put(CATEGORY, category);
        db.insert(DICTIONARY_TABLE_NAME, PLAYER_NAME + ", " + PLAYER_SCORE + ", " + CATEGORY, content);
    }

    public String getValues(SQLiteDatabase db){
        Cursor allRows = db.rawQuery("SELECT * FROM " + DICTIONARY_TABLE_NAME + ";", null);
        allRows.moveToFirst();
        String tableString="";
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
