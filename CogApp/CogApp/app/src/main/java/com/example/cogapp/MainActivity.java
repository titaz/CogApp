package com.example.cogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.example.cogapp.Adapters.ContactAdapter;
import com.example.cogapp.Model.ContactModel;
import com.example.cogapp.ui.main.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        contextOfApplication = getApplicationContext();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }


    public static Context getContextOfApplication(){
        return contextOfApplication;
    }


}