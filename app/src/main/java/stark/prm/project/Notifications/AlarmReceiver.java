package stark.prm.project.Notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import stark.prm.project.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        System.out.println("RECIVER WURDE ANGESPROCHEN");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification_channel")
                .setSmallIcon(R.drawable.ic_icon)
                .setContentTitle("Scheduled Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
            Log.d(TAG, "Notification sent");
        } else {
            Log.d(TAG, "NotificationManager is null");
        }
        notificationManager.notify(0, builder.build());
    }





}

