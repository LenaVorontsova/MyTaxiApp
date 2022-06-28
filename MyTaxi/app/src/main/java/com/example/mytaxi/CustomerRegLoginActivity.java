package com.example.mytaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CustomerRegLoginActivity extends AppCompatActivity {

    TextView enterDriverTV, questionTV;
    Button enterButton, regButton;
    EditText emailET, passET;

    FirebaseAuth mAuth;

    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg_login);

        enterDriverTV = (TextView) findViewById(R.id.enterForCustomerTV);
        questionTV = (TextView) findViewById(R.id.questionCustomerTV);
        enterButton = (Button) findViewById(R.id.signInCustomerButton);
        regButton = (Button) findViewById(R.id.signUpCustomerButton);
        emailET = (EditText) findViewById(R.id.customerEmail);
        passET = (EditText) findViewById(R.id.customerPassword);

        loadingDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        regButton.setVisibility(View.INVISIBLE);
        regButton.setEnabled(false);

        questionTV.setOnClickListener(view -> {
            regButton.setVisibility(View.VISIBLE);
            regButton.setEnabled(true);
            questionTV.setVisibility(View.INVISIBLE);
            enterButton.setVisibility(View.INVISIBLE);
            enterButton.setEnabled(false);
            enterDriverTV.setText("Регистрация для клиента");
        });

        regButton.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String password = passET.getText().toString();

            registerCustomer(email, password);
        });

        enterButton.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String password = passET.getText().toString();

            enterCustomer(email, password);
        });
    }

    private void enterCustomer(String email, String password) {
        loadingDialog.setTitle("Вход клиента");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                CustomerRegLoginActivity.this,
                                "Вход прошел успешно!",
                                Toast.LENGTH_SHORT
                        ).show();
                        loadingDialog.dismiss();
                        Intent customerIntent = new Intent(
                                CustomerRegLoginActivity.this,
                                CustomersMapActivity.class
                        );
                        startActivity(customerIntent);
                    } else {
                        Toast.makeText(
                                CustomerRegLoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                        loadingDialog.dismiss();
                    }
                });
    }

    private void registerCustomer(String email, String password) {
        loadingDialog.setTitle("Регистрация клиента");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                CustomerRegLoginActivity.this,
                                "Регистрация прошла успешно!",
                                Toast.LENGTH_SHORT
                        ).show();
                        loadingDialog.dismiss();
                        Intent customerIntent = new Intent(
                                CustomerRegLoginActivity.this,
                                CustomersMapActivity.class
                        );
                        startActivity(customerIntent);
                    } else {
                        Toast.makeText(
                                CustomerRegLoginActivity.this,
                                "Ошибка при попытке регистрации!",
                                Toast.LENGTH_SHORT
                        ).show();
                        loadingDialog.dismiss();
                    }
                });
    }
}