package com.example.uploadimage;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.application_permissions.Permissions;
import com.example.plate_recognition.RecognizedPlateInfo;
import com.example.restapi.AccessEndpoints;
import com.example.utils.ImageHandler;
import com.example.utils.PropertiesManager;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_GALLERY_CODE = 100;
    private static final int PICK_CAMERA_CODE = 101;

    private ImageView imageView;
    private Button chooseImage;
    private Button sendToLpr;
    private Button getOwnerInfo;
    private TextView textView;
    private TextView recognitionResultPlaceholder;
    private Uri pictureUri;

    private String filePath;

    private File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permissions.verifyPermissions(MainActivity.this);

        imageView = findViewById(R.id.imageView);
        chooseImage = findViewById(R.id.chooseImage);

        sendToLpr = findViewById(R.id.sendToLpr);
        sendToLpr.setEnabled(false);
        textView = findViewById(R.id.textView);
        recognitionResultPlaceholder = findViewById(R.id.recognitionResultPlaceholder);

        getOwnerInfo = findViewById(R.id.getOwnerInfo);
        getOwnerInfo.setEnabled(false);


        chooseImage.setOnClickListener(v -> selectImage());

        sendToLpr.setOnClickListener(v -> {
            Log.e("Path to image", filePath);
            getResponse();
        });

        getOwnerInfo.setOnClickListener(v -> showOwnerInfo());

    }

    private void showOwnerInfo(){
        Intent intent = new Intent(MainActivity.this,OwnerInfoPage.class);
        intent.putExtra("Filter",recognitionResultPlaceholder.getText());
        startActivity(intent);
    }

    private void getResponse(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, AccessEndpoints.LPR_URI,
                response -> {
                    Log.e("Success response ", response);
                    textView.setText("Розпізнавання успішне");
                    String licensePlateNumber = RecognizedPlateInfo.getLicensePlate(response);
                    recognitionResultPlaceholder.setText(licensePlateNumber);
                    getOwnerInfo.setEnabled(!licensePlateNumber.equals(RecognizedPlateInfo.NO_STRING_DETECTED));
                }, error -> {
                    textView.setText("Помилка при розпізнаванні");
                    Log.e("Error response", error.getMessage());
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
        final CharSequence[] items ={"Камера", "Галерея", "Скасувати"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setTitle("Додайте зображення")
                .setItems(items, (dialog, which) -> {
                    if (items[which].equals("Камера")){
                        getImageFromCamera();
                    }else if (items[which].equals("Галерея")){
                        pickImageFromGallery();
                    }else if (items[which].equals("Скасувати")){
                        dialog.dismiss();
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
            photoFile = createPhotoFile();
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
            imageView.setImageBitmap(ImageHandler.rotateImage(bitmap,filePath));
            try {
                filePath = ImageHandler.getCompressedFile(photoFile,MainActivity.this).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == RESULT_OK && requestCode == PICK_GALLERY_CODE){
            pictureUri = data.getData();
            imageView.setImageURI(pictureUri);
            filePath = getRealPathFromUri(pictureUri);

            // to get compressed image from gallery
            File file = new File(filePath);
            try {
                filePath =ImageHandler.getCompressedFile(file,MainActivity.this).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendToLpr.setEnabled(true);
    }

}
