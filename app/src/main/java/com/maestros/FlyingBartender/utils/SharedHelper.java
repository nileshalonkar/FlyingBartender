package com.maestros.FlyingBartender.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {


    /*  SharedHelper.putKey(getApplicationContext(), AppConstats.SELECTED_TAGS, "");*/
    /* username = SharedHelper.getKey(getApplicationContext(), AppConstats.S_USERNAME);*/
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static String MY_PREF = "MY_PREF";

    public static void putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public static String getKey(Context context, String Key) {

        sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");


    }

}
