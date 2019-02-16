package com.shivam.appli.sampleUtil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

public class MyNotificationManager {
    private Context mCtx;
    private static MyNotificationManager myNotificationManager;

    private MyNotificationManager(Context context){
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context)
    {
        if(myNotificationManager == null)
        {
            myNotificationManager = new MyNotificationManager(context);
        }
        return myNotificationManager;
    }

    public void displayNotification(String title,String body)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        Intent i = new Intent(mCtx, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager!=null)
        {
            notificationManager.notify(1,builder.build());
        }

    }

}
