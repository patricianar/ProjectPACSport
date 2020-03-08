package com.example.projectpacsport.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectpacsport.DatabaseHelper;
import com.example.projectpacsport.R;

public class RegisterActivity extends AppCompatActivity {
    Intent loginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);


        final EditText editTextEmail = findViewById(R.id.input_email);
        final EditText editTextFirstName = findViewById(R.id.input_firstName);
        final EditText editTextLastName = findViewById(R.id.input_lastName);
        final EditText editTextPassword = findViewById(R.id.input_password);
        final EditText editTextConfirmPassword = findViewById(R.id.input_confirm_password);

        Button buttonRegister = findViewById(R.id.btn_register);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fN = editTextFirstName.getText().toString();
                String lN = editTextLastName.getText().toString();
                String pw1 = editTextPassword.getText().toString();
                String pw2 = editTextConfirmPassword.getText().toString();
                String email = editTextEmail.getText().toString();
                String result = databaseHelper.validateRegisterData(fN, lN, pw1, pw2, email);
                if (result.equals("Success")){
                    long resultReg = databaseHelper.addUser(fN, lN, pw1, email);
                    if (resultReg != -1){
                        Toast.makeText(RegisterActivity.this, "You have successfully registered \n You will now be redirected to LogIn Page.", Toast.LENGTH_LONG).show();
                        loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);

                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3500); // wait before going to login
                                    startActivity(loginIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
