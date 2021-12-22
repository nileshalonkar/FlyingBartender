package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONObject;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.LOGIN;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SIGNUP;

public class NewLoginActivity extends AppCompatActivity {
    EditText etEmail, etPwd;
    Button btnLogin;
    TextView signup;
    TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPwd);
        signup = findViewById(R.id.signup);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewLoginActivity.this, NewSignUpActivity.class));

            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(NewLoginActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                } else if (etPwd.getText().toString().trim().isEmpty()) {
                    Toast.makeText(NewLoginActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                } else {
                    Login();
                }
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(NewLoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    public void Login() {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", LOGIN)
                .addBodyParameter("email", etEmail.getText().toString().trim())
                .addBodyParameter("password", etPwd.getText().toString().trim())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("gvfgvf", "onResponse: " + response.toString());
                        try {
                            if (response.getString("result").equals("login successfully")) {
                                Toast.makeText(NewLoginActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                                SharedHelper.putKey(NewLoginActivity.this, AppConstats.USER_ID, response.getString("userID"));
                                startActivity(new Intent(NewLoginActivity.this, BottomNavActivity.class));
                                Log.e("jdflfxdvl", response.getString("userID"));

                            } else {
                                Toast.makeText(NewLoginActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.e("ndcmznxdsdx", "onResponse: " + e.getMessage());


                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("iiswis", "onError: " + anError);

                    }
                });

    }
}