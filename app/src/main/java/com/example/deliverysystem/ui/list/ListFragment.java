
package com.example.deliverysystem.ui.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliverysystem.CustomerInfoActivity;
import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.Database.DBschema;
import com.example.deliverysystem.Database.DBschema.Task;
import com.example.deliverysystem.MainActivity;
import com.example.deliverysystem.R;
import com.example.deliverysystem.ScanQRActivity;

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* disable back key*/) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        // Initialize data list
        dataList = new ArrayList<>();

        // Initialize DBHelper
        dbHelper = new DBHelper(getActivity());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Credential", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String vehicleNumber = dbHelper.getVehicleByUsername(username);

        // Set up RecyclerView
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ListAdapter(new ArrayList<>(), new ListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(getActivity(), CustomerInfoActivity.class);
                intent.putExtra("DoNo", task.doNo);
                intent.putExtra("CustName", task.custName);
                intent.putExtra("CustAddress", task.custAddress);
                intent.putExtra("CustContact", task.custContact);
                startActivity(intent);
            }
        });
        List<Task> taskList = dbHelper.getTasksByVehicleNumber(vehicleNumber);
        listAdapter.setTasks(taskList);
        recyclerView.setAdapter(listAdapter);
        return root;
    }
}
