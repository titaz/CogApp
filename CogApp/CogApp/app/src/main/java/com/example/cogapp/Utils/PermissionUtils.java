package com.example.cogapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils{

    private static final int REQUEST_CODE = 1111;

    private Context context;

    private PermissionCallback callback;

    private PermissionUtils(Context context) {
        this.context = context;
    }

    public static PermissionUtils getInstance(Context context){
        return new PermissionUtils(context);
    }

    public void requestPermission(@NonNull String permission){
        if (ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED){
            if (callback != null){
                callback.success();
            }
        }else {
            ActivityCompat.requestPermissions((Activity) context,new String[]{permission},REQUEST_CODE);
        }
    }

    public PermissionUtils setCallback(PermissionCallback callback) {
        this.callback = callback;
        return this;
    }

    public interface PermissionCallback{

        int REQUEST_CODE = 1234;

        void success();
        void fail();
    }

}
