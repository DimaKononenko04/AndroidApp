package com.example.model_manager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Entity {
    private static Gson gson;

    public static <T> T getContent(String jsonString, T type){
        gson = new Gson();
        return gson.fromJson(jsonString, (Type) type.getClass());
    }

}
