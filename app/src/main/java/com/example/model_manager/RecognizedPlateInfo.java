package com.example.model_manager;

import com.example.plate_recognition.model.LprInfo;

public class RecognizedPlateInfo {
    public static String getLicensePlate(String response){
        return Entity.getContent(response, LprInfo.getLPRInfo()).getResults().get(0).getPlate();
    }
}
