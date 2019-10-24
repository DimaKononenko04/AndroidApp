package com.example.plate_recognition.model;

import com.google.gson.annotations.SerializedName;


import java.util.List;

public class LprInfo {

    transient private static LprInfo LPRInfo;

    public static LprInfo getLPRInfo(){
        if (LPRInfo == null){
            LPRInfo = new LprInfo();
        }
        return LPRInfo;
    }

    @SerializedName("processing_time")
    private double processingTime;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("results")
    private List<Result> results;
    @SerializedName("filename")
    private String fileName;
    @SerializedName("version")
    private int version;
    @SerializedName("camera_id")
    private String cameraId;

    public LprInfo() {
    }

    public LprInfo(double processingTime, String timestamp, List<Result> results, String fileName, int version, String cameraId) {
        this.processingTime = processingTime;
        this.timestamp = timestamp;
        this.results = results;
        this.fileName = fileName;
        this.version = version;
        this.cameraId = cameraId;
    }

    public static void setLPRInfo(LprInfo LPRInfo) {
        LprInfo.LPRInfo = LPRInfo;
    }

    public double getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(double processingTime) {
        this.processingTime = processingTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }
}
