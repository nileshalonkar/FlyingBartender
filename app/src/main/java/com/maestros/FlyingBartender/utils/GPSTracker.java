package com.maestros.FlyingBartender.utils;


import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class GPSTracker extends Service  implements LocationListener {


    private Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 0;
    private static final long MIN_TIME_BETWEEN_UPDATE = 1000 * 60 * 1;


    protected LocationManager locationManager;


    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }



    public Location getLocation() {

        try {

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {

                // Means GPS & Network is not available

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.
                            NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGPSEnabled) {

                    if (location == null) {

                        locationManager.requestLocationUpdates(LocationManager.
                                GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {

            Log.e("sraeashgadf", e.getMessage());
        }


        return location;
    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();

            Log.e("GPSTracker", "getLatitude: " +latitude);
        }


        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();

            Log.e("GPSTracker", "getLongitude: " +longitude);
        }

        return longitude;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }






    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
