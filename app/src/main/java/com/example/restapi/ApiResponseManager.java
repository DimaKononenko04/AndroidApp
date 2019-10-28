package com.example.restapi;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.plate_recognition.model.LprInfo;
import com.example.plate_recognition.model_manager.Entity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponseManager {

    public static <T extends AppCompatActivity> LprInfo[] getResponse(T activity,String filePath){
        final LprInfo[] content = new LprInfo[1];
        RequestQueue queue = Volley.newRequestQueue(activity);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, AccessEndpoints.LPR_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Success response ", response);
                        // does not work - null object in content
                        content[0] = Entity.getContent(response, LprInfo.getLPRInfo());
                        Log.e("Content",content[0].toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error during recognition : ", error.getMessage());
            }
        }){
            @Override
            public Map<String,String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", "Token " + AccessConfig.API_KEY);
                return params;
            }
        };
        Log.e("Filepath",filePath);
        smr.addFile("upload", filePath);
        queue.add(smr);
        return content;
    }
}
