package stark.prm.project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import stark.prm.project.data.Database;
import stark.prm.project.data.Homework;
import stark.prm.project.data.Lecture;
import stark.prm.project.data.Module;
import stark.prm.project.uiHelper.UiDatePicker;
import stark.prm.project.uiHelper.UiSeekBar;
import stark.prm.project.uiHelper.UiSideMenu;
import stark.prm.project.uiHelper.UiSpinner;


public class HomeworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homework);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homework_drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpUiElements();
        handleButton();
    }

    private void setUpUiElements(){
        //Initial setup of Spinner Elements
        Spinner modules = findViewById(R.id.homework_spinner_module);
        modules.setAdapter(new UiSpinner(this).createSpinnerElements());

        //Initial setup of Seekbar and Adjacent TextView
        UiSeekBar uiSeekBar = new UiSeekBar();
        TextView seekText = findViewById(R.id.homework_seek_bar_progress_text);
        SeekBar seekbar = findViewById(R.id.homework_seek_bar_progress);
        String initSeekText = "Fortschritt:   " + seekbar.getProgress() + "/10";
        seekText.setText(initSeekText);
        uiSeekBar.handleSeekBar(seekbar, seekText);

        //Initial setup of DatePicker
        UiDatePicker uiDatePicker = new UiDatePicker();
        uiDatePicker.handleDatePicker(findViewById(R.id.homework_edit_text_due_date), this);

        //Initial setup of SideBar-Navigation-Menu
        UiSideMenu uiSideMenu = new UiSideMenu(this,findViewById(R.id.homework_drawer_layout));
        Toolbar toolbar = findViewById(R.id.homework_toolbar);
        setSupportActionBar(toolbar);
        uiSideMenu.handleSideMenu(findViewById(R.id.nav_homework_view),toolbar, getSupportActionBar());
    }

    private void handleButton(){
        Button buttonAdd = findViewById(R.id.btn_add_homework);
        buttonAdd.setOnClickListener(view -> addHomework());
    }

    private void addHomework() {
        Spinner spinner =  findViewById(R.id.homework_spinner_module);
        EditText editLecture = findViewById(R.id.homework_edit_text_lecture);
        String topic = editLecture.getText().toString();
        EditText dueDate = findViewById(R.id.homework_edit_text_due_date);
        EditText pageNum = findViewById(R.id.homework_edit_text_number);
        EditText editDesc = findViewById(R.id.homework_edit_text_description);
        SeekBar seekBar = findViewById(R.id.homework_seek_bar_progress);
        Database db = Database.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        Date date = new Date();
        try {
            date = formatter.parse(dueDate.getText().toString());
        } catch (ParseException ignore) {
            Toast.makeText(this, "Error bei Datum", Toast.LENGTH_SHORT).show();
        }

        Module module = db.getModuleByName(spinner.getSelectedItem().toString());
        if(module == null) {
            throw new RuntimeException("selected Module doesn't exist");
        }

        Lecture lecture = db.getLectureByTopic(topic);
        if(lecture == null) {
            lecture = new Lecture(module, topic);
            db.add(lecture);
        }

        Homework homework = new Homework(
                editDesc.getText().toString(),
                lecture,
                Integer.parseInt(pageNum.getText().toString()),
                seekBar.getProgress(),
                date
        );
        db.add(homework);
    }
}