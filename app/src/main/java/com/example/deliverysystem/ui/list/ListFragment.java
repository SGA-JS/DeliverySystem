// ListFragment.java
package com.example.deliverysystem.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deliverysystem.R;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<String> dataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize data list for test purposes
        dataList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            dataList.add("Item list" + i);
        }

        // Set up RecyclerView
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter(dataList);
        recyclerView.setAdapter(listAdapter);

        return root;
    }
}
