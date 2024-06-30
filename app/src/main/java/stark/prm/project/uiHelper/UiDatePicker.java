package stark.prm.project.uiHelper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class UiDatePicker {

    public void handleDatePicker(EditText dueDate, Context context) {
        Locale.setDefault(Locale.GERMANY);
        Calendar cal = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DAY_OF_MONTH,day);
            updateLabel(dueDate, cal);
        };
        dueDate.setOnClickListener(view -> {
            DatePickerDialog datepicker = new DatePickerDialog(context, date,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            datepicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datepicker.show();
        });
    }
    private void updateLabel(EditText dueDate, Calendar cal){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.GERMANY);
        dueDate.setText(dateFormat.format(cal.getTime()));
    }
}
