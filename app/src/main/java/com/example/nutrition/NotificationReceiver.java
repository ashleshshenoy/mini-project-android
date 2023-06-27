package com.example.nutrition;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "activated ! ", Toast.LENGTH_SHORT).show();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification();
    }
    class NotificationHelper {


        String CHANNEL_ID = "Channel_id";
        int NOTIFICATION_ID = 12;
        int NOTIFICATIONID=13;
        private Context mContext;
        private static final String NOTIFICATION_CHANNEL_ID = "10001";

        NotificationHelper(Context context) {
            mContext = context;

        }

        void createNotification()
        {


            Date date = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            String notificationDescription = "";
            int time = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
            if(time < 10) {
                notificationDescription = "☀️ Its time to start your day with a healthy breakfast. Log your breakfast details now!";
            }else if(time < 12){
                notificationDescription = " \uD83C\uDF6A Its time to grab a quick snack to keep your day going! Log your snack details now!";
            }else if(time < 2 ){
                notificationDescription = "\uD83C\uDF71 Its time to have a delightful, healthy lunch. Log your lunch details now!";
            }else if(time < 6){
                notificationDescription = "\uD83E\uDD64 Grab a quick healthy evening snack. Log your snack details now!";
            }
            else
                notificationDescription = "\uD83C\uDF57 Time for the last meal of the day! Log your dinner details now!";



            Intent intent = new Intent(mContext, calorie.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Its time to track your meals \uD83C\uDF49")
                    .setContentText(notificationDescription)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = mContext.getString(R.string.app_name);
                String description = mContext.getString(R.string.tab_text_1);
                int importance = NotificationManager.IMPORTANCE_MAX;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());


        }

    }

}

