package com.example.batteryandpocketdetction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnCharging;
    TextView txtHowMany, txtbatteryPer;
    ProgressBar progressBar;
    SharedPref sharedPref;
    IntentFilter ifilter;
    private BatteryBroadCastReceiver broadCastReceiver;
    int deviceIn = 0, deviceOut = 0;
    ConstraintLayout linearLayout;
    boolean dec = false;
//

    //For Vibration
    Vibrator vibrator;
    VibratorManager vibratorManager;
    private static final long[] VIBRATE_PATTERN = {500, 500};


    //For pocket sensor
    SensorListener sensorListener;
    Context context = this;
    MediaPlayer mp[] = new MediaPlayer[12];
    TextView inAndOut;


    int pocket = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCharging = findViewById(R.id.button_charge);
        txtbatteryPer = findViewById(R.id.txtbatteryPer);
        progressBar = findViewById(R.id.progressBar);
        txtHowMany = findViewById(R.id.txtHowMany);
        inAndOut = findViewById(R.id.inAndOut);
        linearLayout = findViewById(R.id.linearLayout);
        sensorListener = new SensorListener(this, context);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        broadCastReceiver = new BatteryBroadCastReceiver(this);


        prepare();
        sharedPref = new SharedPref(MainActivity.this);


        Toast.makeText(this, "" + broadCastReceiver.isCharging, Toast.LENGTH_SHORT).show();
//        btnCharging.setOnClickListener(view -> {
//
//            txtHowMany.setText("Charging Status :" + broadCastReceiver.isCharging + "\n" + "Battery plugged AC :" + broadCastReceiver.acCharge + "\n" + "Battery plugged USB :" + broadCastReceiver.usbCharge);
//
//            txtbatteryPer.setText("Battery level " + broadCastReceiver.level);
//            progressBar.setProgress(broadCastReceiver.level);
//
//        });

    }


    @Override
    protected void onStart() {
        registerReceiver(broadCastReceiver, ifilter);


        super.onStart();


    }

    @Override
    protected void onStop() {

        unregisterReceiver(broadCastReceiver);
        super.onStop();

    }

    public void detect(float prox, float light, float g[], int inc) {
        if ((prox < 1) && (light < 2) && (g[1] < -0.6) && ((inc > 75) || (inc < 100))) {
            pocket = 1;
            deviceIn++;

            sharedPref.saveTimesIn(deviceIn);
        }
        if ((prox >= 1) && (light >= 2) && (g[1] >= -0.7)) {
            if (pocket == 1) {
                playSound();

                pocket = 0;

            }
            if (Build.VERSION.SDK_INT>=31) {
                vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager.getDefaultVibrator();
            }
            else {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN,0));
            }
            else {
                vibrator.vibrate(VIBRATE_PATTERN,0);
            }
            deviceOut++;
            sharedPref.saveTimesOut(deviceOut);
        }


    }


    public void playSound() {
        int i = (int) (Math.random() * 10);
        try {
            if (mp[i].isPlaying()) {
                mp[i].stop();
            }
            mp[i].start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void prepare() {
        for (int i = 0; i < 10; i++) {
            mp[i] = MediaPlayer.create(context, R.raw.app_src_main_res_raw_s0);
            String s = "s" + i;
            Log.v("MMM", s);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + s);
            mp[i] = MediaPlayer.create(context, uri);
        }
    }

 /*   private void showNotification() {

        // intent triggered, you can add other intent for other actions
        Intent i = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        //Notification sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            mNotification = new Notification.Builder(this)

                    .setContentTitle("Pocket!")
                    .setContentText("You are punched-in for more than 10hrs!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .addAction(R.drawable.ic_launcher_background, "Goto App", pIntent)
                    .build();

        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

  */

}

