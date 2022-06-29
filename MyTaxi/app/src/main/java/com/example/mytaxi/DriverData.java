package com.example.mytaxi;

public class DriverData {
    String surname, name, patr, login, number, carBrand, color, status, curLocation;

    public DriverData() {
    }

    public DriverData(String surname, String name, String patr, String login, String number, String carBrand, String color, String status, String curLocation) {
        this.surname = surname;
        this.name = name;
        this.patr = patr;
        this.login = login;
        this.number = number;
        this.carBrand = carBrand;
        this.color = color;
        this.status = status;
        this.curLocation = curLocation;
    }
}
