package com.example.deliverysystem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.Database.DBschema.User;
import com.example.deliverysystem.Database.DBschema.Task;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void insertTask() {

        DBHelper dbHelper = mock(DBHelper.class);
        when(dbHelper.insertTask(11010, "Tester123", "4010 Ang Mo Kio Ave 10 Singapore 569626", "97578157", "GBE1234F"))
                .thenReturn(1L);

        long result = dbHelper.insertTask(11010, "Tester123", "4010 Ang Mo Kio Ave 10 Singapore 569626", "97578157", "GBE1234F");

        assertEquals(1L, result);
    }
}