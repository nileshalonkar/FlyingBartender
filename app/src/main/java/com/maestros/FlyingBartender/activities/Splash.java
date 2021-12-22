package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.Connectivity;
import com.maestros.FlyingBartender.utils.SharedHelper;

import java.util.List;

public class Splash extends AppCompatActivity {
    String strUserID = "";
    RelativeLayout noConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        strUserID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        noConnection = findViewById(R.id.noConnection);
        Connectivity connectivity = new Connectivity(this);
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (connectivity.isOnline()) {
                                    if (strUserID.equals("")) {
                                        startActivity(new Intent(Splash.this, NewLoginActivity.class));
                                        finish();
                                    } else {

                                        startActivity(new Intent(Splash.this, BottomNavActivity.class));
                                        finish();
                                    }
                                    noConnection.setVisibility(View.GONE);
                                } else {

                                    noConnection.setVisibility(View.VISIBLE);

                                }

                            }


                        }, 2000);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
