package com.example.uploadimage;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.plate_recognition.RecognizedPlateInfo;
import com.example.restapi.AccessEndpoints;
import com.example.utils.PropertiesManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1;

    private ImageView imageView;
    private Button chooseImage;
    private Button sendToLpr;
    private TextView textView;
    private TextView recognitionResultPlaceholder;
    private Uri contentUri;

    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermissions();

        imageView = findViewById(R.id.imageView);
        chooseImage = findViewById(R.id.chooseImage);
        sendToLpr = findViewById(R.id.sendToLpr);
        textView = findViewById(R.id.textView);
        recognitionResultPlaceholder = findViewById(R.id.recognitionResultPlaceholder);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        sendToLpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = getRealPathFromUri(contentUri);
                Log.e("Path to image", filePath);
                getResponse();
            }
        });

    }

    private void verifyPermissions(){
        Log.e("Permissions","Asking user for permissions");
        String[] permissions =
                {   Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
    }

    private void getResponse(){
        RequestQueue queue = Volley.newRequestQueue(this);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, AccessEndpoints.LPR_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Success response ", response);
                        textView.setText("Recognition Successful");
                        recognitionResultPlaceholder.setText(RecognizedPlateInfo.getLicensePlate(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Error during recognition");
                Log.e("Error response", error.getMessage());
            }
        }){
            @Override
            public Map<String,String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                try {
                    params.put("Authorization", "Token " + PropertiesManager.getProperty("api_token",getApplicationContext()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        Log.e("Filepath",filePath);
        smr.addFile("upload", filePath);
        queue.add(smr);
    }

    private String getRealPathFromUri(Uri contentUri){
        String [] proj ={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj,
                null,
                null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void pickImageFromGallery(){
        // intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            // set image to view
            contentUri = data.getData();
            imageView.setImageURI(data.getData());
        }
    }

}
