
package com.example.deliverysystem.ui.list;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.Database.DBschema.Task;
import com.example.deliverysystem.R;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<String> dataList;
    private DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize data list
        dataList = new ArrayList<>();

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Retrieve tasks from database
        importTasksDatabase();

        // Set up RecyclerView
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter(dataList);
        recyclerView.setAdapter(listAdapter);

        return root;
    }

    private void importTasksDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Task.TASK_TABLE_NAME, null);
        String taskInfohead = "DO No\t\t\t\t\t" +  "Customer\t\t\t\t\t" + "Contact\t\t\t\t\t";
       dataList.add(taskInfohead);
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Extract task data from cursor
                    int doNo = cursor.getInt(cursor.getColumnIndex(Task.TASK_DO_NO));
                    String customerName = cursor.getString(cursor.getColumnIndex(Task.TASK_CUSTOMER_NAME));
                    String contact = cursor.getString(cursor.getColumnIndex(Task.TASK_CONTACT));


                    // Create a string representation of the task and add it to the list

                    String taskInfo = doNo +"\t\t\t\t\t" + customerName + "\t\t\t\t\t" + contact + "\t\t\t\t\t" ;
                    dataList.add(taskInfo);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
    }
}
