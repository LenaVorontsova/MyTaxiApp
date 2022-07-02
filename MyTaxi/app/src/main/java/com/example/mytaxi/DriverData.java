package com.example.mytaxi;

import com.yandex.mapkit.geometry.Point;

public class DriverData {
    String surname, name, patr, login, number, carBrand, color, status;
    Double curLatitude, curLongitude;

    public DriverData() {
    }

    public DriverData(String surname, String name, String patr, String login, String number, String carBrand, String color, String status, Point point) {
        this.surname = surname;
        this.name = name;
        this.patr = patr;
        this.login = login;
        this.number = number;
        this.carBrand = carBrand;
        this.color = color;
        this.status = status;
        this.curLatitude = point.getLatitude();
        this.curLongitude = point.getLongitude();
    }
}
