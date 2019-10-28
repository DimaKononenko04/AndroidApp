package com.example.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.example.uploadimage.MainActivity;

public class LayersManager {

    public  static <T extends AppCompatActivity> void goToMainScreen(T activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
