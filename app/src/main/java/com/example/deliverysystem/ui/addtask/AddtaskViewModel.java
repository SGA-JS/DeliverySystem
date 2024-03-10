package com.example.deliverysystem.ui.addtask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddtaskViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddtaskViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is Addtask fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}