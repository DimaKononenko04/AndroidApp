package com.example.plate_recognition;

import com.example.plate_recognition.model.LprInfo;
import com.example.plate_recognition.model_manager.Entity;

public class RecognizedPlateInfo {
    public static final String NO_STRING_DETECTED ="номерний знак не знайдено";

    public static String getLicensePlate(String response){
        return !Entity.getContent(response, LprInfo.getLPRInfo()).getResults().isEmpty()
                ? Entity.getContent(response, LprInfo.getLPRInfo()).getResults().get(0).getPlate().toUpperCase()
                :NO_STRING_DETECTED;
    }

}
