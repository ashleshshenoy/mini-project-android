package com.example.nutrition;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {


    private int NOTIFICATION_PERMISSION_CODE = 1;


    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_splash_screen);





        askpermission();




    }

    public void myAlarm(int id, int hour, int min, int sec) {

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, min);
        time.set(Calendar.SECOND, sec);


        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }

    }


    void askpermission() {
        if (ContextCompat.checkSelfPermission(
                SplashScreen.this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            setAlarmsForNotifications();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
                    if (sh.contains("username")) {

                        Intent i = new Intent(SplashScreen.this, Home.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            }, 2000);


        }
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        setAlarmsForNotifications();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
                if (sh.contains("username")) {

                    Intent i = new Intent(SplashScreen.this, Home.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        }, 1000);
    }


    void setAlarmsForNotifications(){
        SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
        if(sh.getBoolean("setnotification", true && ContextCompat.checkSelfPermission(
                SplashScreen.this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED)){

            Toast.makeText(SplashScreen.this, "Notifications are set", Toast.LENGTH_LONG).show();
            myAlarm(0, 8, 0, 0);
            myAlarm(1, 11, 0, 0);
            myAlarm(2, 1, 0, 0);
            myAlarm(3, 18, 0, 0);
            myAlarm(4, 20, 30, 0);
            SharedPreferences.Editor editorSh = sh.edit();
            editorSh.putBoolean("setnotification", false);
            editorSh.commit();
        }
    }

}