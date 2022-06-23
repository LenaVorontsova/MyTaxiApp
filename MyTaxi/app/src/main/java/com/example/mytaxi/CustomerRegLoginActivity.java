package com.example.mytaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passET.getText().toString();

                RegisterCustomer(email, password);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passET.getText().toString();

                EnterCustomer(email, password);
            }
        });
    }

    private void EnterCustomer(String email, String password) {
        loadingDialog.setTitle("Вход клиента");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(CustomerRegLoginActivity.this, "Вход прошел успешно!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
                else {
                    Toast.makeText(CustomerRegLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });
    }

    private void RegisterCustomer(String email, String password) {
        loadingDialog.setTitle("Регистрация клиента");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(CustomerRegLoginActivity.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
                else {
                    Toast.makeText(CustomerRegLoginActivity.this, "Ошибка при попытке регистрации!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });
    }
}