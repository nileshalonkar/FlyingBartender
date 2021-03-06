package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.os.Bundle;

import com.freddygenicho.mpesa.stkpush.Mode;
import com.freddygenicho.mpesa.stkpush.model.Mpesa;
import com.maestros.FlyingBartender.R;

public class MpesaPaymentActivity extends AppCompatActivity  {
/*
    //TODO: Replace these values from
    public static final String BUSINESS_SHORT_CODE = "YOUR_SHORTCODE";
    public static final String PASSKEY = "YOUR_PASSKEY";
    public static final String CONSUMER_KEY = "YOUR_KEY";
    public static final String CONSUMER_SECRET = "YOUR_SECRET";
    public static final String CALLBACK_URL = "YOUR_CALLBACK_URL";

    public static final String  NOTIFICATION = "PushNotification";
    public static final String SHARED_PREFERENCES = "com.bdhobare.mpesa_android_sdk";

    Button pay;
    ProgressDialog dialog;
    EditText phone;
    EditText amount;*/

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_payment);

     ///   Mpesa mpesa = new Mpesa(YOUR_CONSUMER_KEY, YOUR_CONSUMER_SECRET, Mode.SANDBOX);


     /*   pay = (Button)findViewById(R.id.pay);
        phone = (EditText)findViewById(R.id.phone);
        amount = (EditText)findViewById(R.id.amount);
        Mpesa.with(this, CONSUMER_KEY, CONSUMER_SECRET);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Processing");
        dialog.setIndeterminate(true);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = phone.getText().toString();
                int a = Integer.valueOf(amount.getText().toString());
                if (p.isEmpty()){
                    phone.setError("Enter phone.");
                    return;
                }
                pay(p, a);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = phone.getText().toString();
                int a = Integer.valueOf(amount.getText().toString());
                if (p.isEmpty()){
                    phone.setError("Enter phone.");
                    return;
                }
                pay(p, a);
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFICATION)) {
                    String title = intent.getStringExtra("title");
                    String message = intent.getStringExtra("message");
                    int code = intent.getIntExtra("code", 0);
                    showDialog(title, message, code);

                }
            }
        };*/
    }

  /*  @Override
    public void onAuthError(Pair<Integer, String> result) {
        Log.e("Error", result.message);
    }

    @Override
    public void onAuthSuccess() {
        //TODO make payment
        pay.setEnabled(true);
    }
    private void pay(String phone, int amount){
        dialog.show();
        STKPush.Builder builder = new Builder(BUSINESS_SHORT_CODE, PASSKEY, amount,BUSINESS_SHORT_CODE, phone);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String token = sharedPreferences.getString("InstanceID", "");

        builder.setFirebaseRegID(token);
        STKPush push = builder.build();



        Mpesa.getInstance().pay(this, push);
    }

    private void showDialog(String title, String message,int code){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        finish();
                    }
                })
                .build();
        View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        ImageView imageView = (ImageView)view.findViewById(R.id.success);
        if (code != 0){
            imageView.setVisibility(View.GONE);
        }
        messageText.setText(message);
        dialog.show();
    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {

    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {

    }*/
}