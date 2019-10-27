package com.example.uploadimage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.application_permissions.Permissions;
import com.example.database_connection.sql_lite.DbHelper;
import com.example.database_connection.sql_lite.DbManager;
import com.example.plate_recognition.RecognizedPlateInfo;
import com.example.restapi.AccessEndpoints;
import com.example.utils.PropertiesManager;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_GALLERY_CODE = 1;
    private static final int PICK_CAMERA_CODE = 0;

    private ImageView imageView;
    private Button chooseImage;
    private Button sendToLpr;
    private Button getOwnerInfo;
    private TextView textView;
    private TextView recognitionResultPlaceholder;
    private Uri pictureUri;

    private String filePath;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permissions.verifyPermissions(MainActivity.this);

        imageView = findViewById(R.id.imageView);
        chooseImage = findViewById(R.id.chooseImage);
        sendToLpr = findViewById(R.id.sendToLpr);
        textView = findViewById(R.id.textView);
        recognitionResultPlaceholder = findViewById(R.id.recognitionResultPlaceholder);

        getOwnerInfo = findViewById(R.id.getOwnerInfo);
        getOwnerInfo.setEnabled(false);

        dbHelper = new DbHelper(this);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        sendToLpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = getRealPathFromUri(pictureUri);
                Log.e("Path to image", filePath);
                getResponse();
                getOwnerInfo.setEnabled(true);
            }
        });

        getOwnerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbManager.viewOwnerInfo(dbHelper,recognitionResultPlaceholder);
                showOwnerInfo();
            }
        });

    }

    private void showOwnerInfo(){
        Intent intent = new Intent(MainActivity.this,OwnerInfoPage.class);
        startActivity(intent);
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
        String [] data ={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                data,
                null,
                null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void selectImage(){
        final CharSequence[] items ={"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setTitle("Add image")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Camera")){
                            getImageFromCamera();
                        }else if (items[which].equals("Gallery")){
                            pickImageFromGallery();
                        }else if (items[which].equals("Cancel")){
                            dialog.dismiss();
                        }
                    }
                });
        builder.show();
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_GALLERY_CODE);
    }

    private void getImageFromCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null) {
            File photoFile = createPhotoFile();
            assert photoFile != null;
            filePath = photoFile.getAbsolutePath();
            pictureUri = FileProvider.getUriForFile(MainActivity.this, "com.example.uploadimage.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(takePictureIntent, PICK_CAMERA_CODE);
        }

    }

    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_CAMERA_CODE) {
            Log.e("Filepath : ",filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imageView.setImageBitmap(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == PICK_GALLERY_CODE){
            pictureUri = data.getData();
            imageView.setImageURI(pictureUri);
        }
    }

}
