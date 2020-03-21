package com.example.projectpacsport.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectpacsport.DatabaseHelper;
import com.example.projectpacsport.MainActivity;
import com.example.projectpacsport.R;
import com.example.projectpacsport.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    // For the layout
    private Context mContext;
    private EditText editTextUsername, editTextPassword;
    private TextView mTextLinkSignUp;
    private AppCompatButton mLoginButton;

    DatabaseHelper myDatabaseHelper;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currentUser = new User();

        // Context
        mContext = LoginActivity.this;

        // Login UI
        editTextUsername = (EditText) findViewById(R.id.login_email);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        mTextLinkSignUp = (TextView) findViewById(R.id.textlink_signup);
        mLoginButton = findViewById(R.id.btn_login);

        // Setting up listener
        mTextLinkSignUp.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTextLinkSignUp) {
            startActivity(new Intent(mContext, RegisterActivity.class));
        } else {
            myDatabaseHelper = new DatabaseHelper(LoginActivity.this);
            String uname = editTextUsername.getText().toString();
            String pword = editTextPassword.getText().toString();

            boolean login = myDatabaseHelper.validation(uname, pword, currentUser);

            if (login == true) {
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Wrong email or password!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

