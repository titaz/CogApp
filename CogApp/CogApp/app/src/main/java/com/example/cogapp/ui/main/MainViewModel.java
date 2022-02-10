package com.example.cogapp.ui.main;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Base64;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public String encodePackageName(String packageName) {
        byte[] encodedBytes = Base64.encode(packageName.getBytes(), Base64.DEFAULT);
        System.out.println("encodedBytes " + new String(encodedBytes));
        return new String(encodedBytes);
    }

    public void decodePackageName(String encoded) {
        byte[] decodedBytes = Base64.decode(encoded,Base64.DEFAULT);
        System.out.println("decodedBytes " + new String(decodedBytes));
    }

}