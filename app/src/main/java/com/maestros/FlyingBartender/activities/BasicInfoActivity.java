package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.maestros.FlyingBartender.databinding.ActivityBasicInfoBinding;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.BASEURL;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SIGNUP;

public class BasicInfoActivity extends AppCompatActivity {
    private ActivityBasicInfoBinding binding;
    private Context context;
    private View view;
    private String email="",pwd="",mobile="",userType="",age="",strName;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBasicInfoBinding.inflate(getLayoutInflater());
        view=binding.getRoot();
        setContentView(view);

        context=this;
        gson=new Gson();
        if (getIntent()!=null){
            email=getIntent().getStringExtra("email");
            pwd=getIntent().getStringExtra("pwd");
            mobile=getIntent().getStringExtra("mobile");
            userType=getIntent().getStringExtra("userType");
            Log.e("BasicInfoActivity", "userType: " +userType);
            age=getIntent().getStringExtra("age");
        }

        binding.btnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddAddressActivity.class));
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName=binding.etName.getText().toString().trim();

                if (TextUtils.isEmpty(strName)){
                    binding.etName.setError("Please enter your name!");
                    binding.etName.requestFocus();
                }else {
                   sendData();
                }
            }
        });
    }

    private void sendData() {
        Log.e("fdgjkhfkgjfhdg",email+"");
        Log.e("fdgjkhfkgjfhdg",mobile+"");
        Log.e("fdgjkhfkgjfhdg",age+"");
        Log.e("fdgjkhfkgjfhdg",strName+"");
        Log.e("fdgjkhfkgjfhdg",pwd+"");
        AndroidNetworking.post(BASEURL)
                .addBodyParameter("control", "signup" )
                .addBodyParameter("email", email)
                .addBodyParameter("mobile", mobile)
                .addBodyParameter("age_validation", age)
                .addBodyParameter("type", "3")
                .addBodyParameter("name", strName)
                .addBodyParameter("password",pwd)
                .setTag("SIGNUP")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("hhdfhdfhfdh", "onResponse: " +response.toString());
                        try {
                            if (response.getString("result").equals("true")){
                                JSONObject jsonObject = new JSONObject(response.getString("data"));
                                SharedHelper.putKey(context, AppConstats.USER_ID, jsonObject.getString("userID"));
                                startActivity(new Intent(context, BottomNavActivity.class));


                               for (int i = 0; i < jsonObject.length(); i++) {
                                    SharedHelper.putKey(context, AppConstats.USER_ID, jsonObject.getString("userID"));
                                    startActivity(new Intent(context, BottomNavActivity.class));

                                    Log.e("sdzsdsd", response.getString("userID") );

                                }

                            }

                            else {
                                Toast.makeText(BasicInfoActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e){
                            Log.e("hhdfhdfhfdh", "onResponse: " +e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("hhdfhdfhfdh", "onError: " +anError);

                    }
                });

            /*   .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("jhdjshd",response+"");

                        try {

                            if (response.getBoolean("result")==true){

                                SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, "");
                                startActivity(new Intent(context, BottomNavActivity.class));

                            }else {

                            }

                            Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Log.e("ijif", "onResponse: " +e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {

                        // handle error
                    }
                });*/
    }
}