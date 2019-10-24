package com.example.plate_recognition.model;

public class Result {
    private Box box;
    private String plate;
    private double score;

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getDscore() {
        return dscore;
    }

    public void setDscore(double dscore) {
        this.dscore = dscore;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    private double dscore;
    private Vehicle vehicle;
}
