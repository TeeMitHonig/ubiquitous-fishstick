package stark.prm.project.Notifications;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;



public class NotificationHelper {

    private static final String CHANNEL_ID = "notification_channel";
    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    /**
     * Creates a notification channel for the alarm manager
     */
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for alarm manager notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Schedules a notification at the specified time
     * @param message the message to be displayed in the notification
     * @param month the month of the notification
     * @param day the day of the notification
     * @param hour the hour of the notification
     * @param minute the minute of the notification
     */
    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(String message,int month,int day, int hour, int minute) {
        Log.d("NotificationHelper", "Scheduling notification for " + day + "/" + month + " at " + hour + ":" + minute);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.MONTH,month);
//        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}