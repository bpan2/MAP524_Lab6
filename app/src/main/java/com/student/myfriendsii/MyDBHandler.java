package com.student.myfriendsii;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


public class MyDBHandler extends SQLiteOpenHelper{

    //need to double check the path
    private static final String DATABASE_PATH = "/data/data/com.student.myfriendsii/databases/";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "friendDB.db";
    private static final String TABLE_FRIENDS = "friends";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONENUMBER = "phoneNumber";
    private static final String DATABASE_CREATE = "create table friends(_id integer primary key autoincrement, name text, email text, phoneNumber text);";


    private final Context myContext;

    public SQLiteDatabase dbSqlite = null;

    public MyDBHandler(Context context, String name,SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void createDatabase(){
        createDB();
    }

    private void createDB(){
        boolean dbExist = DBExists();

        if(!dbExist){
            this.getReadableDatabase();// Important Android built-in dbs functions for importing existing dbs
            copyDBFromResource();
        }
    }

    private boolean DBExists(){
        try{
            String dbsPath = DATABASE_PATH + DATABASE_NAME;
            dbSqlite = SQLiteDatabase.openDatabase(dbsPath, null, SQLiteDatabase.OPEN_READWRITE);
            dbSqlite.setLocale(Locale.getDefault());
            dbSqlite.setLockingEnabled(true);
            dbSqlite.setVersion(1);
        }
        catch (SQLException e){
            Log.e("myDBHandler", "database not found");
        }
        return dbSqlite != null ? true : false;
    }


    private void copyDBFromResource(){
        InputStream is = null;
        OutputStream os = null;
        String dbFilePath = DATABASE_PATH + DATABASE_NAME;

        try{
            is = myContext.getAssets().open(DATABASE_NAME);
            os = new FileOutputStream(dbFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer)) > 0){
                os.write(buffer, 0, length);
            }

            os.flush();
            os.close();
            is.close();
        }
        catch(IOException e){
            throw new Error("Problem at copying dbs from res file");
        }
    }


    //http://stackoverflow.com/questions/11506527/count-the-number-of-tables-in-the-sqlite-database
    public Cursor getTableNameCursor(){
        Cursor c = dbSqlite.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'sqlite_sequence' ", null);
        return c;
    }

    public Cursor getCursor(){
        Cursor myCursor;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_FRIENDS);
        String[] asColumnsToReturn = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONENUMBER};
        myCursor = queryBuilder.query(
                dbSqlite,
                asColumnsToReturn,
                null,
                null,
                null,
                null,
                "name ASC"
        );

        return myCursor;
    }

    public String getName(Cursor c){
        return (c.getString(1));
    }


    //---insert a friend into the database---
    public long insertFriend(String name, String email, Integer phoneNumber)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONENUMBER, phoneNumber);
        return dbSqlite.insert(TABLE_FRIENDS, null, values);
    }


    //---deletes a particular friend---
    public boolean deleteFriend(String name)
    {
        return dbSqlite.delete(TABLE_FRIENDS, COLUMN_NAME + "=" + "name", null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllFriends()
    {

        // if selecting All columns, the second argument could null
        return dbSqlite.query(TABLE_FRIENDS, null, null, null, null, null, null);
    }


    //---updates a contact---
    public boolean updateFriend(int id, String name, String email, int phoneNumber)
    {
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME, name);
        args.put(COLUMN_EMAIL, email);
        args.put(COLUMN_PHONENUMBER, phoneNumber);

        return dbSqlite.update(TABLE_FRIENDS, args, COLUMN_ID + "=" + id, null) > 0;
    }


}
