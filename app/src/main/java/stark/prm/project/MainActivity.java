package stark.prm.project;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import stark.prm.project.Notifications.NotificationHelper;
import stark.prm.project.data.Database;
import stark.prm.project.data.Module;




public class MainActivity extends AppCompatActivity {
    /**
     * Request code for scheduling exact alarms
     */
    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 100;
    private NotificationHelper notificationHelper;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.landing_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Database.getInstance().init(this); // populate the Database with some Modules

        buttonClick();

        try {
            notificationHelper = new NotificationHelper(this);
            notificationHelper.createNotificationChannel();
            Log.d("MainActivity", "NotificationHelper initialized");
            //Neispiel Nottification wird  Temporär drinne egelassen Beidde alse können gelösct werden
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!hasExactAlarmPermission()) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
                } else {
                    notificationHelper.scheduleNotification("Dies ist eine geplante Benachrichtigung", 6, 30, 12, 24);
                }
            }
            //this.startActivity(new Intent(MainActivity.this, HomeworkActivity.class));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error in onCreate: " + e.getMessage());
        }
    }

    /**
     * checks if the app has the permission to schedule exact alarms
     * @return true if the app has the permission, false otherwise
     */
    private boolean hasExactAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return false;
    }

    /**
     * Callback for the result from requesting permissions. This method is invoked for every call on
     * @param requestCode The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            //Kann tehoretisch auch weg ist aber nur ein beispeil test für benachrichtigungen
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Permission granted, scheduling notification");
                notificationHelper.scheduleNotification("Dies ist eine geplante Benachrichtigung", 6, 30, 21, 16);
            } else {
                Log.d("MainActivity", "Permission Not granted, scheduling notification");
                // Berechtigung verweigert, leite Benutzer zu den Einstellungen
                Toast.makeText(this, "Exact Alarm Permission Denied. Please enable it in the settings.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }
    private void buttonClick() {
      Button buttonHomework = findViewById(R.id.btn_landing_homework);
      buttonHomework.setOnClickListener(view -> {
          this.startActivity(new Intent(MainActivity.this, HomeworkListActivity.class));
      });


      Button buttonNotes = findViewById(R.id.btn_landing_notes);
      buttonNotes.setOnClickListener(view -> {
          this.startActivity(new Intent(MainActivity.this, NoteActivity.class));
      });

      Button buttonProgress = findViewById(R.id.btn_landing_progress);
      buttonProgress.setOnClickListener(view -> {
          this.startActivity(new Intent(MainActivity.this, ProgressActivity.class));
      });
    }
}
