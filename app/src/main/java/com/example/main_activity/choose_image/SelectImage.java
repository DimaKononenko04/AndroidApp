package com.example.main_activity.choose_image;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class SelectImage {
// does not work with chain of image selections due to final limitations
    private static final int PICK_GALLERY_CODE = 1;
    private static final int PICK_CAMERA_CODE = 0;

    public static <T extends AppCompatActivity> void selectImage(final T activity, final Uri pictureUri, final String filePath){
        final CharSequence[] items ={"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle("Add image")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Camera")){
                            getImageFromCamera(activity,pictureUri,filePath);
                        }else if (items[which].equals("Gallery")){
                            pickImageFromGallery(activity);
                        }else if (items[which].equals("Cancel")){
                            dialog.dismiss();
                        }
                    }
                });
        builder.show();
    }

    private static  <T extends AppCompatActivity> void pickImageFromGallery(T activity){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_GALLERY_CODE);
    }

    private static  <T extends AppCompatActivity> void getImageFromCamera(T activity, Uri pictureUri, String filePath){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager())!=null) {
            File photoFile = createPhotoFile();
            assert photoFile != null;
            filePath = photoFile.getAbsolutePath();
            pictureUri = FileProvider.getUriForFile(activity, "com.example.uploadimage.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            activity.startActivityForResult(takePictureIntent, PICK_CAMERA_CODE);
        }
    }

    private static File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
