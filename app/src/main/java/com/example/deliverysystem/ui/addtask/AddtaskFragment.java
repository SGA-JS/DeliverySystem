package com.example.deliverysystem.ui.addtask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deliverysystem.ConstantValue;
import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.LoginActivity;
import com.example.deliverysystem.MainActivity;
import com.example.deliverysystem.R;
import com.example.deliverysystem.ScanQRActivity;
import com.example.deliverysystem.SignupActivity;
import com.example.deliverysystem.TaskAssignment;
import com.example.deliverysystem.databinding.FragmentAddtaskBinding;

import java.util.List;

public class AddtaskFragment extends Fragment {

    private FragmentAddtaskBinding binding;
    private DBHelper dbHelper;
    ImageButton qrCamera;
    TextView doNumber, customerName, customerAddress, customerContact;
    Spinner driverList;
    Button btnAssign;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddtaskViewModel galleryViewModel =
                new ViewModelProvider(this).get(AddtaskViewModel.class);

        binding = FragmentAddtaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.address;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* disable back key*/) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);


        qrCamera = root.findViewById(R.id.qrCamera);
        doNumber = root.findViewById(R.id.doNumber);
        customerName = root.findViewById(R.id.name);
        customerAddress = root.findViewById(R.id.address);
        customerContact = root.findViewById(R.id.phoneNumber);
        driverList = root.findViewById(R.id.driverList);
        btnAssign = root.findViewById(R.id.btnAssign);
        qrCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQRActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, ConstantValue.REQUEST_CODE_SCAN_QR);
            }
        });

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DBHelper(requireContext());
                String doNumberText = doNumber.getText().toString().trim();
                String customerNameText = customerName.getText().toString().trim();
                String customerAddressText = customerAddress.getText().toString().trim();
                String customerContactText = customerContact.getText().toString().trim();
                String selectedDriver = (String) driverList.getSelectedItem();
                long result = -1;

                if (!validateScanInput(doNumberText, customerNameText, customerAddressText, customerContactText)) {
                    // If any of the required fields are empty, show a toast to inform the user
                    Toast.makeText(getActivity(), "Please rescan if any information is missing.", Toast.LENGTH_SHORT).show();
                } else if (!validateDriver(selectedDriver)) {
                    // If no driver is selected, show a toast to inform the user to select a driver
                    Toast.makeText(getActivity(), "Please select a driver.", Toast.LENGTH_SHORT).show();
                } else {
                    // All required information is provided, proceed with inserting the task
                    result = dbHelper.insertTask(Integer.valueOf(doNumberText), customerNameText, customerAddressText, customerContactText, selectedDriver);

                    if(result < 0){
                        Toast.makeText(requireContext(), "Failed to add new task", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(requireContext(), "New task added", Toast.LENGTH_SHORT).show();
                        // Clear input fields and reset spinner selection
                        doNumber.setText("");
                        customerName.setText("");
                        customerAddress.setText("");
                        customerContact.setText("");
                        driverList.setSelection(0);
                    }
                }
            }
        });

        return root;
    }

    public boolean validateScanInput(String doNumberText, String customerNameText, String customerAddressText, String customerContactText)
    {
        if (doNumberText.isEmpty() || customerNameText.isEmpty() || customerAddressText.isEmpty() || customerContactText.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validateDriver(String driver)
    {
        if (driver == null || driver.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValue.REQUEST_CODE_SCAN_QR
                && resultCode == ConstantValue.RESPONSE_CODE_SCAN_OK) {
            String scannedText = data.getStringExtra("scannedText");
            //Log.i("Add Task Fragment","Scanned QR value: " + scannedText);
            String[] elements = scannedText.split("\\|");

            if (elements.length == 4) {
                doNumber.setText(elements[0]);
                customerName.setText(elements[1]);
                customerAddress.setText(elements[2]);
                customerContact.setText(elements[3]);
            } else {
                Toast.makeText(getActivity(), "QR code value missing element.", Toast.LENGTH_SHORT).show();
                Log.e("Scan Error","Scanned QR value: " + scannedText + " | Missing element");
            }

            dbHelper = new DBHelper(requireContext());

            // Get the vehicle list from the database
            List<String> vehicleList = dbHelper.getAllVehicleNumbers();

            // Create an ArrayAdapter using the vehicle list and a default spinner layout
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, vehicleList);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            driverList.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}