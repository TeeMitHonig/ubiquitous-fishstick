package stark.prm.project.uiHelper;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import stark.prm.project.R;
import stark.prm.project.data.Database;
import stark.prm.project.data.Lecture;
import stark.prm.project.data.Module;

public class UiSpinner {

    Context context;

    public UiSpinner(Context context) {
        this.context = context;
    }

    public ArrayAdapter<String> createSpinnerElements(){
        List<String> arraySpinner = new ArrayList<>();
        Database db = Database.getInstance();

        HashMap<UUID, Module> moduleMap = db.getModules();
        for (Module item : moduleMap.values()) {
            arraySpinner.add(item.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;

    }
}
