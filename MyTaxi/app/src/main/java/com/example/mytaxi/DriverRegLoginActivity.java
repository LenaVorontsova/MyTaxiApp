package com.example.mytaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class DriverRegLoginActivity extends AppCompatActivity {

    TextView enterDriverTV, questionTV;
    Button enterButton, regButton;
    EditText emailET, passET;

    FirebaseAuth mAuth;

    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reg_login);

        enterDriverTV = (TextView)findViewById(R.id.enterForDriverTV);
        questionTV = (TextView)findViewById(R.id.questionTV);
        enterButton = (Button)findViewById(R.id.signInDriverButton);
        regButton = (Button)findViewById(R.id.signUpDriverButton);
        emailET = (EditText) findViewById(R.id.driverEmail);
        passET = (EditText) findViewById(R.id.driverPassword);

        mAuth = FirebaseAuth.getInstance();

        loadingDialog = new ProgressDialog(this);

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
                enterDriverTV.setText("Регистрация для водителя");
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passET.getText().toString().trim();

                RegisterDriver(email, password);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passET.getText().toString().trim();

                EnterDriver(email, password);
            }
        });
    }

    private void EnterDriver(String email, String password) {
        loadingDialog.setTitle("Вход водителя");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(DriverRegLoginActivity.this, "Вход прошел успешно!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    enterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent driverIntent = new Intent(DriverRegLoginActivity.this, DriversMapActivity.class);
                            startActivity(driverIntent);
                        }
                    });
                }
                else {
                    Toast.makeText(DriverRegLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });
    }

    private void RegisterDriver(String email, String password) {
        loadingDialog.setTitle("Регистрация водителя");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(DriverRegLoginActivity.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    regButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent driverIntent = new Intent(DriverRegLoginActivity.this, DriversMapActivity.class);
                            startActivity(driverIntent);
                        }
                    });
                }
                else {
                    Toast.makeText(DriverRegLoginActivity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });
    }
}