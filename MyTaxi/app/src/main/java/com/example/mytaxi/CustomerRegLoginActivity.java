package com.example.mytaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomerRegLoginActivity extends AppCompatActivity {

    TextView enterDriverTV, questionTV;
    Button enterButton, regButton;
    EditText emailET, passET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg_login);

        enterDriverTV = (TextView)findViewById(R.id.enterForCustomerTV);
        questionTV = (TextView)findViewById(R.id.questionCustomerTV);
        enterButton = (Button)findViewById(R.id.signInCustomerButton);
        regButton = (Button)findViewById(R.id.signUpCustomerButton);
        emailET = (EditText) findViewById(R.id.customerEmail);
        passET = (EditText) findViewById(R.id.customerPassword);

        regButton.setVisibility(View.INVISIBLE);
        regButton.setEnabled(false);

        questionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regButton.setVisibility(View.VISIBLE);
                regButton.setEnabled(true);
                questionTV.setVisibility(View.INVISIBLE);
                enterButton.setVisibility(View.INVISIBLE);
                enterButton.setEnabled(false);
                enterDriverTV.setText("Регистрация для клиента");
            }
        });
    }
}