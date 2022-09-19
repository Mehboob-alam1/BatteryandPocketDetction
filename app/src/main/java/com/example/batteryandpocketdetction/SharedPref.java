package com.example.batteryandpocketdetction;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {


    SharedPreferences sharedPreferences;
    private static final String TIMES_BATTERY_CONNECTED = "con";
    private static final String PHONE_STATUS = "status";

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("con", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("status", Context.MODE_PRIVATE);

    }

    public void saveTimesIn(int timeConnected) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TIMES_BATTERY_CONNECTED, timeConnected);
        editor.apply();
    }

    public int fetchTimesIn() {

        return sharedPreferences.getInt(TIMES_BATTERY_CONNECTED, 0);
    }

    public void saveTimesOut(int status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PHONE_STATUS, status);
        editor.apply();
    }

    public int fetchTimesOut() {
        return sharedPreferences.getInt(PHONE_STATUS, 0);
    }
}
