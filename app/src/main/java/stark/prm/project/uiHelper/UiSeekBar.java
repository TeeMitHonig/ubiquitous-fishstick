package stark.prm.project.uiHelper;

import android.widget.SeekBar;
import android.widget.TextView;

import stark.prm.project.R;

public class UiSeekBar {

    public void handleSeekBar(SeekBar seekbar, TextView seekText){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekbar.getProgress();
                String text = "Fortschritt: ";
                if(progress < 10) text += "  ";
                text += progress + "/10";
                seekText.setText(text);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
