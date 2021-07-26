package com.example.coachlea.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.coachlea.R;

public class NotificationReceiver extends BroadcastReceiver {
    //comment

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Notifier notifications =new Notifier(context);
        if (intent.getAction() != null && context != null) {

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                notifications.setReminder(context, NotificationReceiver.class,
                        9, 0);
                return;

            }

        }

        String Title_notification=context.getResources().getString(R.string.title_notification);
        String content_notification=context.getResources().getString(R.string.content_notify);

        //Trigger the notification
        notifications.notifyUser(Title_notification, content_notification);

    }
}
