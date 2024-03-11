package com.example.deliverysystem.Database;

import static com.example.deliverysystem.Database.DBschema.Task;
import static com.example.deliverysystem.Database.DBschema.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.deliverysystem.ConstantValue;

import java.util.ArrayList;
import java.util.List;


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
                    + Task.TASK_DO_NO + " INTEGER PRIMARY KEY, "
                    + Task.TASK_CUSTOMER_NAME + " TEXT NOT NULL, "
                    + Task.TASK_ADDRESS + " TEXT NOT NULL, "
                    + Task.TASK_CONTACT + " TEXT NOT NULL, "
                    + Task.TASK_DRIVER + " TEXT NOT NULL, "
                    + Task.TASK_STATUS + " INTEGER NOT NULL, "
                    + Task.TASK_IMAGE_PATH + " TEXT, "
                    + Task.TASK_DELIEVRED_TIMESTAMP + " TEXT,"
                    + "CONSTRAINT DONo_unique UNIQUE (" + Task.TASK_DO_NO + "));";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + User.USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Task.TASK_TABLE_NAME);
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

    public int getRoleIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int roleId = -1; // Default value if role ID is not found

        String[] projection = {User.USER_ROLE};
        String selection = User.USER_USERNAME + " = ?";
        String[] selectionArgs = {username};

        cursor = db.query(
                User.USER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(User.USER_ROLE);
                if (columnIndex != -1) { // Check if the column exists
                    roleId = cursor.getInt(columnIndex);
                } else {
                    // Handle column not found error
                    Log.e("DatabaseHelper", "Column 'USER_ROLE' not found");
                }
            }
            cursor.close();
        }

        return roleId;
    }

    public String getVehicleByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String vehicle = null;

        String[] projection = {User.USER_VEHICLE_NO};
        String selection = User.USER_USERNAME + " = ?";
        String[] selectionArgs = {username};

        cursor = db.query(
                User.USER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(User.USER_VEHICLE_NO);
                if (columnIndex != -1) { // Check if the column exists
                    vehicle = cursor.getString(columnIndex);
                } else {
                    // Handle column not found error
                    Log.e("DatabaseHelper", "Column 'USER_VEHICLE_NO' not found");
                }
            }
            cursor.close();
        }

        return vehicle;
    }

    public List<String> getAllVehicleNumbers() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> vehicleNumbers = new ArrayList<>();

        // Define the columns you want to retrieve
        String[] projection = {User.USER_VEHICLE_NO};

        // Query the database
        Cursor cursor = db.query(
                User.USER_TABLE_NAME, projection, null, null, null, null, null);

        // Iterate through the cursor and add vehicle numbers to the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex(User.USER_VEHICLE_NO);
                if (columnIndex >= 0) {
                    // Valid column index, retrieve column value
                    String vehicleNumber = cursor.getString(columnIndex);
                    vehicleNumbers.add(vehicleNumber);
                } else {
                    // Handle error: Column not found
                    Log.e("DatabaseHelper", "Column USER_VEHICLE_NO not found in cursor");
                }
            }
        }

        // Close the cursor
        cursor.close();

        return vehicleNumbers;
    }

    public long insertTask(int doNo, String customerName, String address, String contact, String driver) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Task.TASK_DO_NO, doNo);
        values.put(Task.TASK_CUSTOMER_NAME, customerName);
        values.put(Task.TASK_ADDRESS, address);
        values.put(Task.TASK_CONTACT, contact);
        values.put(Task.TASK_DRIVER, driver);
        values.put(Task.TASK_STATUS, ConstantValue.TASK_STATUS_UNDONE);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Task.TASK_TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public boolean updateTaskProcessing(String doNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Task.TASK_STATUS, ConstantValue.TASK_STATUS_PROCESSING);

        // Update the status for the specified task ID
        int rowsAffected = db.update(Task.TASK_TABLE_NAME, values, Task.TASK_DO_NO + " = ?", new String[]{doNo});
        db.close();

        // Check if any rows were affected by the update
        return rowsAffected > 0;
    }

    public boolean completeTask(String doNo, String imagePath, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Task.TASK_STATUS, ConstantValue.TASK_STATUS_COMPLETED);
        values.put(Task.TASK_IMAGE_PATH, imagePath);
        values.put(Task.TASK_DELIEVRED_TIMESTAMP, dateTime);

        // Update the status for the specified task ID
        int rowsAffected = db.update(Task.TASK_TABLE_NAME, values, Task.TASK_DO_NO + " = ?", new String[]{doNo});
        db.close();

        // Check if any rows were affected by the update
        return rowsAffected > 0;
    }

    public int getTaskCountByStatus(int role, String vehicleNumber, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        // Define the SQL query to count the number of tasks in undone status for a specific driver
        String query = "SELECT COUNT(*) FROM " + Task.TASK_TABLE_NAME +
                " WHERE " + Task.TASK_STATUS + " = ?";

        // Initialize selection arguments with undone status
        List<String> selectionArgsList = new ArrayList<>();
        selectionArgsList.add(String.valueOf(status));

        // Add additional condition for driver role
        if (role == ConstantValue.ROLE_DRIVER) {
            query += " AND " + Task.TASK_DRIVER + " = ?";
            selectionArgsList.add(vehicleNumber);
        }

        // Convert selectionArgsList to an array
        String[] selectionArgs = new String[selectionArgsList.size()];
        selectionArgsList.toArray(selectionArgs);

        // Execute the query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Retrieve the count from the cursor
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

        // Close the database connection
        db.close();

        return count;
    }

    public int getTotalTaskCountByAccount(int role, String vehicleNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        // Define the SQL query to count the number of tasks in undone status for a specific driver
        String query = "SELECT COUNT(*) FROM " + Task.TASK_TABLE_NAME;

        // Initialize selection arguments with undone status
        List<String> selectionArgsList = new ArrayList<>();

        // Add additional condition for driver role
        if (role == ConstantValue.ROLE_DRIVER) {
            query += " WHERE " + Task.TASK_DRIVER + " = ?";
            selectionArgsList.add(vehicleNumber);
        }

        // Convert selectionArgsList to an array
        String[] selectionArgs = new String[selectionArgsList.size()];
        selectionArgsList.toArray(selectionArgs);

        // Execute the query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Retrieve the count from the cursor
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

        // Close the database connection
        db.close();

        return count;
    }
}






