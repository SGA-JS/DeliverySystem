package com.example.deliverysystem.Database;

import static com.example.deliverysystem.Database.DBschema.*;
import static com.example.deliverysystem.Database.DBschema.AdminEntry.*;
import static com.example.deliverysystem.Database.DBschema.CustomerEntry.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    static int DATABASE_VERSION = 2;
    private Context context;

    private static final String DATABASE_NAME = "MyDatabase.db";

    private static final String SQL_CUSTOMER_TABLE =
            "CREATE TABLE " + CUSTOMER_TABLE + " (" +
                    CUSTOMER_COLUMN_ID + " INTEGER PRIMARY KEY," +
                    CUSTOMER_COLUMN_NAME + " TEXT," +
                    CUSTOMER_COLUMN_EMAIL + " TEXT," +
                    CUSTOMER_COLUMN_PHONE + " TEXT," +
                    CUSTOMER_COLUMN_ADDRESS + " TEXT," +
                    CUSTOMER_COLUMN_PASSWORD + " TEXT)";

    private static final String SQL_ADMIN_TABLE =
            "CREATE TABLE " + ADMIN_TABLE + " (" +
                    CUSTOMER_COLUMN_ID + " INTEGER PRIMARY KEY," +
                    ADMIN_COLUMN_USERNAME + " TEXT," +
                    ADMIN_COLUMN_PASSWORD + " TEXT)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CUSTOMER_TABLE);
        db.execSQL(SQL_ADMIN_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade

        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ADMIN_TABLE);
        onCreate(db);
    }
    public boolean insertCustomer(String name, String email, String phone, String address, String password) {
        SQLiteDatabase db = getWritableDatabase();

        //Check if the email already exists in the database
        if (checkDuplicateUser(email)) {
            // User already exists, return false
            return false;
        }

        //row to insert
        ContentValues contentValues = new ContentValues();
        contentValues.put(CustomerEntry.CUSTOMER_COLUMN_NAME, name);
        contentValues.put(CustomerEntry.CUSTOMER_COLUMN_EMAIL, email);
        contentValues.put(CustomerEntry.CUSTOMER_COLUMN_PHONE, phone);
        contentValues.put(CustomerEntry.CUSTOMER_COLUMN_ADDRESS, address);
        contentValues.put(CustomerEntry.CUSTOMER_COLUMN_PASSWORD, password);

        long result = db.insert("customers", null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    //check duplicate user
    private boolean checkDuplicateUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to check if the email already exists
        Cursor cursor = db.query(CUSTOMER_TABLE,
                new String[]{CUSTOMER_COLUMN_EMAIL},
                CUSTOMER_COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        // Check if the cursor has any rows (i.e., if the email exists)
        boolean exists = cursor.getCount() > 0;

        // Close the cursor and return the result
        cursor.close();
        return exists;
    }

    public boolean updateCustomer (Integer id, String name, String phone, String email, String address, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the new values
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", address);
        contentValues.put("password", password);

        // run the update query
        db.update("customers", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteCustomer (Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ADMIN_TABLE,
                ADMIN_COLUMN_ID + " = ?" ,  new String[] { Integer.toString(id) });

        // delete contact
        return db.delete(CUSTOMER_TABLE,
                CUSTOMER_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
    }


    /**
     * Get the list of all contacts
     * @return
     */
    public ArrayList<Pair<Integer, String>> getAllContacts()
    {
        ArrayList<Pair<Integer, String>> array_list = new ArrayList<Pair<Integer, String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from customers", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add( new Pair(res.getInt(res.getColumnIndex(CUSTOMER_COLUMN_ID) ), res.getString(res.getColumnIndex(CUSTOMER_COLUMN_NAME))) );
            res.moveToNext();
        }
        return array_list;
    }


    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );

        Cursor res =  db.rawQuery( "select " + CUSTOMER_TABLE + ".*, sum(" + ADMIN_TABLE + "." + ADMIN_COLUMN_USERNAME + ") " +
                "from " +  CUSTOMER_TABLE + ", " + ADMIN_TABLE + " " +
                "where " + CUSTOMER_TABLE + "." + CUSTOMER_COLUMN_ID + "=" + id  + " and " + ADMIN_TABLE +  "." + ADMIN_COLUMN_ID + " = " + id, null );



        // select contacts.*, sum(donations.amount_donated) from contacts, donations where contacts.id = id  and  donations.contact_id = id


        return res;
    }


    /**
     * Count the number of rows in a table
     * @return
     */
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();

        return  (int) DatabaseUtils.queryNumEntries(db, CUSTOMER_TABLE);

    }


    //checkUser
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Define the columns to be retrieved
            String[] columns = {CUSTOMER_COLUMN_ID};
            // Define the selection criteria
            String selection = CUSTOMER_COLUMN_EMAIL + " = ? AND " + CUSTOMER_COLUMN_PASSWORD + " = ?";
            // Define the selection arguments
            String[] selectionArgs = {email, password};
            // Query the database
            cursor = db.query(CUSTOMER_TABLE, columns, selection, selectionArgs, null, null, null);
            // Check if any row is returned
            if (cursor != null && cursor.getCount() > 0) {
                return true; // User exists and credentials match
            }
        } finally {
            // Close the cursor to release resources
            if (cursor != null) {
                cursor.close();
            }
        }
        return false; // User does not exist or credentials do not match
    }



}






