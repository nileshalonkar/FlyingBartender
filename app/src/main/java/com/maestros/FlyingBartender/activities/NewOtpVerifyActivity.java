package com.maestros.FlyingBartender.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.LOGIN;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.OTPVERIFY;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SIGNUP;
import static com.maestros.FlyingBartender.utils.AppConstats.MOBILE_NUMBER;
import static com.maestros.FlyingBartender.utils.AppConstats.PASSWORD;

public class NewOtpVerifyActivity extends AppCompatActivity {
    Button btnVerify;
    String user_id = "", OTP = "";
    TextView tvMail;
    String Email_Id = "", Mobile_Number = "", Password = "";

    private String mVerificationId;
    private FirebaseAuth mAuth;
    PinView firstPinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_otp_verify);
        firstPinView = findViewById(R.id.firstPinView);
        btnVerify = findViewById(R.id.btnVerify);
        tvMail = findViewById(R.id.tvMail);

        mAuth = FirebaseAuth.getInstance();

     /*   user_id = SharedHelper.getKey(NewOtpVerifyActivity.this, AppConstats.USER_ID);
        Log.e("ssss", user_id);
*/

        Email_Id = SharedHelper.getKey(NewOtpVerifyActivity.this, AppConstats.EMAIL_ID);
        Log.e("ewfewfwe", Email_Id);

        Mobile_Number = SharedHelper.getKey(NewOtpVerifyActivity.this, MOBILE_NUMBER);
        sendVerificationCode(Mobile_Number);
        Log.e("ewfewfwe", Mobile_Number);

        Password = SharedHelper.getKey(NewOtpVerifyActivity.this, PASSWORD);
        Log.e("ewfewfwe", Password);


/*        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String mobile = intent.getStringExtra("mobile");
        String password = intent.getStringExtra("password");
        sendVerificationCode(mobile);
        Log.e("frgdfgfd", email );
        Log.e("frgdfgfd", mobile );
        Log.e("frgdfgfd", password );*/


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = firstPinView.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    firstPinView.setError("Enter valid code");
                    firstPinView.requestFocus();
                    return;
                }else {
                    //verifying the code entered manually
                    verifyVerificationCode(code);

                }

            }
        });
    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                firstPinView.setText(code);
                //verifying the code
                Log.e("sgddfdfdfdf", code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(NewOtpVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        Log.e("fsdadsffddfd", code + "code");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        Log.e("fsdadsffddfd", mVerificationId + "mVerificationId");
        Log.e("fsdadsffddfd", credential + "credential");
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(NewOtpVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SignUp();
                          //  verifyOtp();
                            //verification successful we will start the profile activity
                           /* Intent intent = new Intent(NewOtpVerifyActivity.this, BottomNavActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/

                        } else {
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                Toast.makeText(NewOtpVerifyActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }else {
                                SignUp();
                             //   verifyOtp();
                            }

                         /*   Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();*/
                        }
                    }
                });

    }
    public void SignUp() {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SIGNUP)
                .addBodyParameter("email",Email_Id )
                .addBodyParameter("mobile",Mobile_Number)
                .addBodyParameter("password",Password)
                .addBodyParameter("age", "1")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dkscksdc", "onResponse: " + response.toString());
                        try {
                            if (response.getString("result").equals("otp sent successfully")) {
                                Toast.makeText(NewOtpVerifyActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                                SharedHelper.putKey(NewOtpVerifyActivity.this, AppConstats.USER_ID, response.getString("userID"));
                               // startActivity(new Intent(NewOtpVerifyActivity.this, NewOtpVerifyActivity.class));
                                startActivity(new Intent(NewOtpVerifyActivity.this, BottomNavActivity.class));
                            } else {
                                Toast.makeText(NewOtpVerifyActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();

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

    public void verifyOtp() {
  /*      AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", OTPVERIFY)
                .addBodyParameter("id", user_id)
                //   .addBodyParameter("otp", firstPinView.getText().toString().trim())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("kglfbgkfng", "onResponse: " + response.toString());
                        try {
                            if (response.getString("result").equals("otp match successfully")) {
                                Toast.makeText(NewOtpVerifyActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NewOtpVerifyActivity.this, BottomNavActivity.class));

                            } else {
                                Toast.makeText(NewOtpVerifyActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.e("sdjxksmd", "onResponse: " + e.getMessage());


                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("vhgvhjbg", "onError: " + anError);

                    }
                });

*/
    }
}