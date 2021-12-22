package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.Api;
import com.google.android.material.button.MaterialButton;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONObject;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.CHANGE_PASSWORD;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etCurrentPassword,etNewPassword,etConfirmPassword;
    MaterialButton btnChangePass;
    String User_Id="";

    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePass = findViewById(R.id.btnChangePass);
        imgBack = findViewById(R.id.imgBack);

        User_Id = SharedHelper.getKey(ChangePasswordActivity.this, AppConstats.USER_ID);
        Log.e("dfgdsdd",User_Id );

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });
    }

    private void validateDetails() {
        if (etCurrentPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter current password", Toast.LENGTH_SHORT).show();
        } else if (etNewPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
        } else if(etConfirmPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
        } else {
            ChangePassword();
        }
    }

    private void ChangePassword() {
     //   progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", CHANGE_PASSWORD)
                .addBodyParameter("userID",User_Id)
                .addBodyParameter("old_password",etCurrentPassword.getText().toString())
                .addBodyParameter("new_password",etNewPassword.getText().toString())
                .addBodyParameter("confirm_password",etConfirmPassword.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                   //     progressBar.setVisibility(View.GONE);
                        Log.e("frrrte", "onResponse: "+response);
                        try {
                            if (response.getString("result").equals("update successfully")){
                                Toast.makeText(ChangePasswordActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangePasswordActivity.this, BottomNavActivity.class));

                            }else {
                                Toast.makeText(ChangePasswordActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Log.e("frrrte", e.getMessage());
                        //    progressBar.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("sadDSDSSSD", anError.getMessage());
                      //  progressBar.setVisibility(View.GONE);

                    }
                });
    }
}