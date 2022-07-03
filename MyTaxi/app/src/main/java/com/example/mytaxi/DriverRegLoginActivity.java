package com.example.mytaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DriverRegLoginActivity extends AppCompatActivity {

    private TextView enterDriverTV, questionTV;
    private Button enterButton, regButton;
    private EditText emailET, passET;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingDialog;

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

        regButton.setOnClickListener(view -> {
            String email = emailET.getText().toString().trim();
            String password = passET.getText().toString().trim();

            registerDriver(email, password);
        });

        enterButton.setOnClickListener(view -> {
            String email = emailET.getText().toString().trim();
            String password = passET.getText().toString().trim();

            enterDriver(email, password);
        });
    }

    private void enterDriver(String email, String password) {
        loadingDialog.setTitle("Вход водителя");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(
                        DriverRegLoginActivity.this,
                        "Вход прошел успешно!",
                        Toast.LENGTH_SHORT)
                        .show();
                loadingDialog.dismiss();
                Intent driverIntent = new Intent(
                        DriverRegLoginActivity.this,
                        DriversInfoActivity.class
                );
                startActivity(driverIntent);
            }
            else {
                Toast.makeText(
                        DriverRegLoginActivity.this,
                        task.getException().getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
                loadingDialog.dismiss();
            }
        });
    }

    private void registerDriver(String email, String password) {
        loadingDialog.setTitle("Регистрация водителя");
        loadingDialog.setMessage("Пожалуйста, дождитесь загрузки!");
        loadingDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(
                        DriverRegLoginActivity.this,
                        "Регистрация прошла успешно!",
                        Toast.LENGTH_SHORT)
                        .show();
                loadingDialog.dismiss();
                Intent driverIntent = new Intent(
                        DriverRegLoginActivity.this,
                        DriversInfoActivity.class);
                startActivity(driverIntent);
            }
            else {
                Toast.makeText(
                        DriverRegLoginActivity.this,
                        task.getException().getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
                loadingDialog.dismiss();
            }
        });
    }
}