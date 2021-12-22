package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.databinding.ActivityVerifyBinding;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.Connectivity;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.OTPVERIFY;

public class VerifyActivity extends AppCompatActivity {
  /*  private ActivityVerifyBinding binding;
    private Context context;
    private View view;
    private String email="",pwd="",mobile="",userType="",age="",strPin;
    Connectivity connectivity;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* binding=ActivityVerifyBinding.inflate(getLayoutInflater());
        view=binding.getRoot();
        setContentView(view);

        context=this;
        connectivity=new Connectivity(context);
       // if (getIntent()!=null){
            email=getIntent().getStringExtra("email");
            pwd=getIntent().getStringExtra("pwd");
            mobile=getIntent().getStringExtra("mobile");
            userType=getIntent().getStringExtra("userType");
            age=getIntent().getStringExtra("age");

            Log.e("VerifyActivity", "onCreate: " +email);
            Log.e("VerifyActivity", "onCreate: " +pwd);
            Log.e("VerifyActivity", "onCreate: " +mobile);
            Log.e("VerifyActivity", "onCreate: " +userType);
            Log.e("VerifyActivity", "onCreate: " +age);
        //

        binding.tvMail.setText("A verification code sent to "+email);

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("3")){
                    strPin=binding.firstPinView.getText().toString();
                    if (connectivity.isOnline()){
                        sendData();
                    }else {

                    }

                }else {
                    startActivity(new Intent(context, AccountHelpActivity.class).putExtra("come_from", "1"));
                    finish();
                }
            }
        });*/
    }

  /*  private void sendData() {
      CustomDialog dialog=new CustomDialog();
        dialog.showDialog(R.layout.progress_layout,this);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control",OTPVERIFY )
                .addBodyParameter("email", email)
                .addBodyParameter("otp", strPin)
                .setTag("OTPVERIFY")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hideDialog();
                        Log.e("fdjfkd",response+"");

                        try {

                            if (response.getString("result").equals("true")){

                                startActivity(new Intent(VerifyActivity.this, BasicInfoActivity.class));
                               *//* String data=response.getString("data");
                                JSONObject jsonObject=new JSONObject(data);*//*
                                      *//*  SharedHelper.putKey(VerifyActivity.this, AppConstats.USER_ID, response.getString("userID"));
                                       Log.e("userID", response.getString("userID"));*//*
                                       *//* SharedHelper.putKey(VerifyActivity.this, AppConstats.USER_NAME, response.getString("name"));
                                         Log.e("fdggsd", response.getString("name"));*//*
                                        SharedHelper.putKey(VerifyActivity.this,AppConstats.USER_EMAIL, response.getString("email"));
                                         Log.e("email", response.getString("email"));
                                        *//*SharedHelper.putKey(VerifyActivity.this, AppConstats.USER_PASSWORD, response.getString("password"));
                                        SharedHelper.putKey(VerifyActivity.this, AppConstats.USER_MOBILE, response.getString("mobile"));
                                        SharedHelper.putKey(VerifyActivity.this, AppConstats.USER_TYPE, response.getString("type"));
                                        SharedHelper.putKey(VerifyActivity.this, AppConstats.USER_AGE, response.getString("age_validation"));*//*

                                         finish();

                                     *//*           .putExtra("email", email)
                                        .putExtra("pwd", pwd)
                                        .putExtra("mobile", mobile)
                                        .putExtra("userType", userType)
                                        .putExtra("age", age));*//*


                            }else {
                                Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }


                        } catch (JSONException e) {
                            Log.e("gbhhbfyg", "e: " +e.getMessage());
                            dialog.hideDialog();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e("rfrf", "error: " +error.getMessage());
                        dialog.hideDialog();
                    }
                });
    }*/
}