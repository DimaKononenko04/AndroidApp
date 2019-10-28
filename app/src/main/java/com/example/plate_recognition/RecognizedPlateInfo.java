package com.example.plate_recognition;

import com.example.plate_recognition.model.LprInfo;
import com.example.plate_recognition.model_manager.Entity;

public class RecognizedPlateInfo {
    public static String getLicensePlate(String response){
        return Entity.getContent(response, LprInfo.getLPRInfo())
                .getResults().get(0).getPlate().toUpperCase();
    }

}
