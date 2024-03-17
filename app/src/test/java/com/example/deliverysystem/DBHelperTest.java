package com.example.deliverysystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.deliverysystem.Database.DBHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBHelperTest {
    Context context;
    DBHelper dbHelper;
    //SQLiteDatabase db;

    @Before
    public void setup()
    {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbHelper = new DBHelper(context);
        //db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);
    }
    @Test
    public void testInsertUser() {
        long result = dbHelper.insertUser("testUser", "password123", 0, "ABC123");
        assertTrue(result > 0);
    }

    @Test
    public void testCheckUser()
    {
        dbHelper.insertUser("testUser", "password123", 0, "ABC123");
        boolean userExists = dbHelper.checkUser("testUser", "password123");
        assertTrue(userExists);
    }

    @Test
    public void testGetRoleIdByUsername()
    {
        dbHelper.insertUser("testUser", "password123", 0, "ABC123");
        int roleId = dbHelper.getRoleIdByUsername("testUser");
        assertEquals(0, roleId);
    }

    @Test
    public void testVehicleByUsername()
    {
        dbHelper.insertUser("testUser", "password123", 0, "ABC123");
        String vehicle = dbHelper.getVehicleByUsername("testUser");
        assertEquals("ABC123", vehicle);
    }

    @Test
    public void testInsertTask()
    {
        long result = dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateTaskProcessing()
    {
        dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        boolean result = dbHelper.updateTaskProcessing(11010);
        assertTrue(result);
    }

    @Test
    public void testCompleteTask()
    {
        dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        boolean result = dbHelper.completeTask(11010, "2024-01-01");
        assertTrue(result);
    }

    @Test
    public void testUpdateImage()
    {
        dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        boolean result = dbHelper.updateImage(11010, "path/to/image");
        assertTrue(result);
    }

    @Test
    public void testGetTaskStatusByDo()
    {
        dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        dbHelper.completeTask(11010, "2024-01-01");
        int status = dbHelper.getTaskStatusByDo(11010);
        assertEquals(ConstantValue.TASK_STATUS_COMPLETED, status);
    }

    @Test
    public void testGetTaskCountByStatus()
    {
        dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        int count = dbHelper.getTaskCountByStatus(ConstantValue.ROLE_DRIVER, "ABC123", ConstantValue.TASK_STATUS_UNDONE);
        assertEquals(1, count);
    }

    @Test
    public void testGetTotalTaskCountByAccount()
    {
        dbHelper.insertTask(11010, "Tester123", "8 Wilkie Rd, Singapore 228095", "99998888", "ABC123");
        int count = dbHelper.getTotalTaskCountByAccount(ConstantValue.ROLE_DRIVER, "ABC123");
        assertEquals(1, count);
    }
}