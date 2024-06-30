package stark.prm.project;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import stark.prm.project.Notifications.NotificationHelper;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 100;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel();

        //Neispiel Nottification wird  Temporär drinne egelassen Beidde alse können gelösct werden
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasExactAlarmPermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            } else {
                notificationHelper.scheduleNotification("Dies ist eine geplante Benachrichtigung", 5, 14);
            }
        } else {
            notificationHelper.scheduleNotification("Dies ist eine geplante Benachrichtigung", 5, 14);
        }
    }

    private boolean hasExactAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            //Kann tehoretisch auch weg ist aber nur ein beispeil test für benachrichtigungen
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notificationHelper.scheduleNotification("Dies ist eine geplante Benachrichtigung", 5, 14);
            } else {
                // Berechtigung verweigert, leite Benutzer zu den Einstellungen
                Toast.makeText(this, "Exact Alarm Permission Denied. Please enable it in the settings.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }
}
