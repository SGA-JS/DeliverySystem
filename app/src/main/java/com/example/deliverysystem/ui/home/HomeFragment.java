package com.example.deliverysystem.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deliverysystem.ConstantValue;
import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.MainActivity;
import com.example.deliverysystem.R;
import com.example.deliverysystem.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ProgressBar progressUndone, progressProcessing, progressCompleted;
    TextView txtTotal, txtUndone, txtProcessing, txtCompleted;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        int undoneCount = 0, processingCount = 0, completedCount = 0, totalTask = 0, progress = 0;

        progressUndone = root.findViewById(R.id.undoneProgress);
        progressProcessing = root.findViewById(R.id.processingProgress);
        progressCompleted = root.findViewById(R.id.completedProgress);
        txtTotal = root.findViewById(R.id.txtTotalTask);
        txtUndone = root.findViewById(R.id.txtUndone);
        txtProcessing = root.findViewById(R.id.txtProcessing);
        txtCompleted = root.findViewById(R.id.txtCompleted);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Credential", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        DBHelper myDB = new DBHelper(requireContext());
        int roleId = myDB.getRoleIdByUsername(username);
        if(roleId >= 0) {
            if (roleId == ConstantValue.ROLE_ADMIN) {
                // Get the task count by status
                undoneCount = myDB.getTaskCountByStatus(ConstantValue.ROLE_ADMIN, null,ConstantValue.TASK_STATUS_UNDONE);
                processingCount = myDB.getTaskCountByStatus(ConstantValue.ROLE_ADMIN, null,ConstantValue.TASK_STATUS_PROCESSING);
                completedCount = myDB.getTaskCountByStatus(ConstantValue.ROLE_ADMIN, null,ConstantValue.TASK_STATUS_COMPLETED);
                totalTask = myDB.getTotalTaskCountByAccount(ConstantValue.ROLE_ADMIN, null);
            } else if (roleId == ConstantValue.ROLE_DRIVER) {
                String vehicle = myDB.getVehicleByUsername(username);

                // Get the task count by status
                undoneCount = myDB.getTaskCountByStatus(ConstantValue.ROLE_DRIVER, vehicle, ConstantValue.TASK_STATUS_UNDONE);
                processingCount = myDB.getTaskCountByStatus(ConstantValue.ROLE_DRIVER, vehicle, ConstantValue.TASK_STATUS_PROCESSING);
                completedCount = myDB.getTaskCountByStatus(ConstantValue.ROLE_DRIVER, vehicle, ConstantValue.TASK_STATUS_COMPLETED);
                totalTask = myDB.getTotalTaskCountByAccount(ConstantValue.ROLE_DRIVER, vehicle);
            }
            txtTotal.setText("Total Task: " + String.valueOf(totalTask));

            // Set progress and text for undone tasks
            progress = totalTask == 0 ? 0 : ((undoneCount/totalTask)*100);
            progressUndone.setProgress(progress);
            txtUndone.setText("Undone: " + String.valueOf(undoneCount));

            // Set progress and text for processing tasks
            progress = totalTask == 0 ? 0 : ((processingCount/totalTask)*100);
            progressProcessing.setProgress(progress);
            txtProcessing.setText("Processing: " + String.valueOf(processingCount));

            // Set progress and text for completed tasks
            progress = totalTask == 0 ? 0 : ((completedCount/totalTask)*100);
            progressCompleted.setProgress(progress);
            txtCompleted.setText("Completed: " + String.valueOf(completedCount));
        }

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}