package com.example.cogapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AndroidUtils {
    public static String NEWLINE = "\n";
    public static void makeText (Context context,
                                  CharSequence text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}
