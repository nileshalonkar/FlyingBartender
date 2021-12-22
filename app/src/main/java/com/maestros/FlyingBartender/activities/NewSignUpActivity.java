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
import com.google.android.gms.common.api.Api;
import com.google.android.material.textfield.TextInputLayout;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONObject;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_BANNER;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SIGNUP;

public class NewSignUpActivity extends AppCompatActivity {
    EditText etEmail, etMobile, etPassword;
    Button btn_SignUp;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sign_up);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btn_SignUp = findViewById(R.id.btn_SignUp);
        tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewSignUpActivity.this, NewLoginActivity.class));

            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    etEmail.setError("Enter a valid email");
                    etEmail.requestFocus();
                    return;
                } else if (mobile.isEmpty() || mobile.length() < 10) {
                    etMobile.setError("Enter a valid mobile");
                    etMobile.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    etMobile.setError("Enter a valid mobile");
                    etMobile.requestFocus();
                    return;
                }else {
                   // SignUp(email,mobile,password);
                    SharedHelper.putKey(NewSignUpActivity.this, AppConstats.EMAIL_ID, email);
                    SharedHelper.putKey(NewSignUpActivity.this, AppConstats.MOBILE_NUMBER, mobile);
                    SharedHelper.putKey(NewSignUpActivity.this, AppConstats.PASSWORD, password);
                    startActivity(new Intent(NewSignUpActivity.this, NewOtpVerifyActivity.class));

                    Log.e("eerwfrf", email);
                    Log.e("eerwfrf", mobile);
                    Log.e("eerwfrf", password);


                }
            }
        });


/*        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty()){
                    etEmail.setError("Enter a valid email");
                    etEmail.requestFocus();
                    return;
                } else if(mobile.isEmpty() || mobile.length() < 10){
                    etMobile.setError("Enter a valid mobile");
                    etMobile.requestFocus();
                    return;
                } else if(password.isEmpty()){
                    etMobile.setError("Enter a valid mobile");
                    etMobile.requestFocus();
                    return;

                }

                Intent intent = new Intent(NewSignUpActivity.this, NewOtpVerifyActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("mobile", mobile);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });*/
    }

    public void SignUp(String email, String mobile,String password) {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SIGNUP)
                .addBodyParameter("email",email )
                .addBodyParameter("mobile",mobile )
                .addBodyParameter("password",password)
                .addBodyParameter("age", "1")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dkscksdc", "onResponse: " + response.toString());
                        try {
                            if (response.getString("result").equals("otp sent successfully")) {
                                Toast.makeText(NewSignUpActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                                SharedHelper.putKey(NewSignUpActivity.this, AppConstats.USER_ID, response.getString("userID"));
                                //  SharedHelper.putKey(NewSignUpActivity.this, AppConstats.EMAIL_ID, response.getString("email"));
                                //   SharedHelper.putKey(NewSignUpActivity.this, AppConstats.MOBILE_NUMBER, response.getString("mobile"));
                                startActivity(new Intent(NewSignUpActivity.this, NewOtpVerifyActivity.class));
                            } else {
                                Toast.makeText(NewSignUpActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception e) {
                            Log.e("skjdks", "onResponse: " + e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fygftftf", "onResponse: " + anError.getMessage());

                    }
                });
    }
}