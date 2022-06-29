package com.example.mytaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DriversInfoActivity extends AppCompatActivity {

    EditText surnameET, nameET, patrET, loginET, numberET, carBrandET, colorET, statusET, curLocationET;
    Button saveButton, toTheMapButton;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Drivers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_info);

        surnameET = (EditText) findViewById(R.id.surnameET);
        nameET = (EditText) findViewById(R.id.nameET);
        patrET = (EditText) findViewById(R.id.patrET);
        loginET = (EditText)findViewById(R.id.loginET);
        numberET = (EditText) findViewById(R.id.numberET);
        carBrandET = (EditText) findViewById(R.id.carBrandET);
        colorET = (EditText) findViewById(R.id.colorET);
        statusET = (EditText) findViewById(R.id.statusET);
        curLocationET = (EditText) findViewById(R.id.currentLocationET);
        saveButton = (Button) findViewById(R.id.saveInfoButton);
        toTheMapButton = (Button) findViewById(R.id.goToTheMapButton);

        toTheMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(
                        DriversInfoActivity.this,
                        DriversMapActivity.class);
                startActivity(mapIntent);
            }
        });
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String surname = surnameET.getText().toString();
                String name = nameET.getText().toString();
                String patr = patrET.getText().toString();
                String login = loginET.getText().toString();
                String number = numberET.getText().toString();
                String carBrand = carBrandET.getText().toString();
                String color = colorET.getText().toString();
                String status = statusET.getText().toString();
                String curLocation = curLocationET.getText().toString();
                DriverData driverData = new DriverData(surname, name, patr, login, number, carBrand, color, status, curLocation);
                saveData(driverData);
            }
        });
    }

    private void saveData(DriverData driverData) {
        HashMap<String, String> userMap = new HashMap<>();

        userMap.put("surname", driverData.surname);
        userMap.put("name", driverData.name);
        userMap.put("patronymic", driverData.patr);
        userMap.put("login", driverData.login);
        userMap.put("number", driverData.number);
        userMap.put("carBrand", driverData.carBrand);
        userMap.put("color", driverData.color);
        userMap.put("status", driverData.status);
        userMap.put("currentLocation", driverData.curLocation);

        root.push().setValue(userMap);

    }
}