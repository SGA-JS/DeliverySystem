package com.example.deliverysystem.Database;

import android.provider.BaseColumns;

public class DBschema {
    // Customer table
    public static class CustomerEntry implements BaseColumns {
        public static final String CUSTOMER_TABLE = "customers";
        public static final String CUSTOMER_COLUMN_ID     = "id";
        public static final String CUSTOMER_COLUMN_NAME = "name";
        public static final String CUSTOMER_COLUMN_EMAIL = "email";
        public static final String CUSTOMER_COLUMN_PHONE = "phone";
        public static final String CUSTOMER_COLUMN_ADDRESS = "address";

        public static final String CUSTOMER_COLUMN_PASSWORD = "password";

        // Add more columns as needed
    }

    // Admin table
    public static class AdminEntry implements BaseColumns {
        public static final String ADMIN_TABLE = "admins";
        public static final String ADMIN_COLUMN_ID     = "id";
        public static final String ADMIN_COLUMN_USERNAME = "username";
        public static final String ADMIN_COLUMN_PASSWORD = "password";
        // Add more columns as needed
    }
}

