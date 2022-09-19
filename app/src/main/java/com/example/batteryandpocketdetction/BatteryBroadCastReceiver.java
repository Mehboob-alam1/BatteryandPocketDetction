package com.example.batteryandpocketdetction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryBroadCastReceiver extends BroadcastReceiver {
    boolean usbCharge;
    boolean acCharge;
    boolean isCharging;
    SharedPref sharedPref;
    int level;
    MainActivity mainActivity;

    public BatteryBroadCastReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        sharedPref = new SharedPref(context);
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        TextView txtbatteryPer = (TextView) this.mainActivity.findViewById(R.id.txtbatteryPer);
        ProgressBar progressBar = (ProgressBar) this.mainActivity.findViewById(R.id.progressBar);
        TextView txtHowMany = (TextView) this.mainActivity.findViewById(R.id.txtHowMany);

        txtHowMany.setText("Charging Status :" + isCharging + "\n" + "Battery plugged AC :" + acCharge + "\n" + "Battery plugged USB :" + usbCharge);

        txtbatteryPer.setText("Battery level " + level);
        progressBar.setProgress(level);


        if (isCharging) {
            Toast.makeText(context, "Charging", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(context, "No charging", Toast.LENGTH_SHORT).show();
        }


    }


}
