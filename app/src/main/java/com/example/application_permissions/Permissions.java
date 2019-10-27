package com.example.application_permissions;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Permissions {
    public static <T extends AppCompatActivity> void verifyPermissions(T activity){
        Log.e("Permissions","Asking user for permissions");
        String[] permissions =
                {   Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(activity,permissions,1);
    }
}
