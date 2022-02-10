package com.example.cogapp.ui.main;

import android.content.Intent;
import android.provider.ContactsContract;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public void openSomeActivityForResult(ActivityResultLauncher<Intent> someActivityResultLauncher) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            someActivityResultLauncher.launch(intent);
    }
    // TODO: Implement the ViewModel
}