package com.example.projectpacsport.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectpacsport.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    // For the layout
    private Context mContext;
    private EditText mEmail, mPassword;
    private TextView mTextLinkSignUp;
    private AppCompatButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Context
        mContext = LoginActivity.this;

        // Login UI
        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);
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
        }
    }
}
