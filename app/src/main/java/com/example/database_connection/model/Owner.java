package com.example.database_connection.model;

public class Owner {
    private String id;
    private String name;
    private String surname;
    private String telephone;
    private String licensePlate;

    public Owner() {
    }

    public Owner(String id, String name, String surname, String telephone, String licensePlate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.licensePlate = licensePlate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
