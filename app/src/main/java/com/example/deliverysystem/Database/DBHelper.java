package com.example.deliverysystem.Database;

import static com.example.deliverysystem.Database.DBschema.Task;
import static com.example.deliverysystem.Database.DBschema.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDatabase.db";
    static int DATABASE_VERSION = 2;
    private Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private static final String SQLITE_CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + User.USER_TABLE_NAME + " ("
                    + User.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + User.USER_USERNAME + " TEXT NOT NULL, "
                    + User.USER_PASSWORD + " TEXT NOT NULL, "
                    + User.USER_ROLE + " INTEGER NOT NULL, "
                    + User.USER_VEHICLE_NO + " TEXT, "
                    + "CONSTRAINT username_unique UNIQUE (" + User.USER_USERNAME + "));";

    private static final String SQLITE_CREATE_TASK_TABLE =
            "CREATE TABLE IF NOT EXISTS " + Task.TASK_TABLE_NAME + " ("
                    + Task.TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Task.TASK_CUSTOMER_NAME + " TEXT NOT NULL, "
                    + Task.TASK_ADDRESS + " TEXT NOT NULL, "
                    + Task.TASK_CONTACT + " TEXT NOT NULL, "
                    + Task.TASK_DRIVER + " TEXT NOT NULL, "
                    + Task.TASK_IMAGE_PATH + " TEXT, "
                    + Task.TASK_DELIEVRED_TIMESTAMP + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS customers");
        db.execSQL("DROP TABLE IF EXISTS admins");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL(SQLITE_CREATE_USER_TABLE);
        db.execSQL(SQLITE_CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade
        db.execSQL("DROP TABLE IF EXISTS " + User.USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Task.TASK_TABLE_NAME);
        onCreate(db);
    }

    public void insertCustomer(String name, String password, int role, String vehicle) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(User.USER_USERNAME, name);
        contentValues.put(User.USER_PASSWORD, password);
        contentValues.put(User.USER_ROLE, role);
        contentValues.put(User.USER_VEHICLE_NO, vehicle);

        long result = db.insert(User.USER_TABLE_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "New user added", Toast.LENGTH_SHORT).show();
        }
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

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Define the columns to be retrieved
            String[] columns = {User.USER_ID};
            // Define the selection criteria
            String selection = User.USER_USERNAME + " = ? AND " + User.USER_PASSWORD + " = ?";
            // Define the selection arguments
            String[] selectionArgs = {username, password};
            // Query the database
            cursor = db.query(User.USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
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






