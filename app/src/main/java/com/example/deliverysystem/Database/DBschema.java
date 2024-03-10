package com.example.deliverysystem.Database;

import android.provider.BaseColumns;

public class DBschema {
    public static class User implements BaseColumns {
        public static final String USER_TABLE_NAME = "User";
        public static final String USER_ID     = "userId";
        public static final String USER_USERNAME = "username";
        public static final String USER_PASSWORD = "password";
        public static final String USER_ROLE = "role";
        public static final String USER_VEHICLE_NO = "vehicleNo";
    }

    public static class Task implements BaseColumns {
        public static final String TASK_TABLE_NAME = "Task";
        public static final String TASK_ID = "taskId";
        public static final String TASK_CUSTOMER_NAME = "customerName";
        public static final String TASK_ADDRESS = "address";
        public static final String TASK_CONTACT = "contact";
        public static final String TASK_DRIVER = "driver";
        public static final String TASK_IMAGE_PATH = "image";
        public static final String TASK_DELIEVRED_TIMESTAMP = "deliveredTimestamp";
    }
}

