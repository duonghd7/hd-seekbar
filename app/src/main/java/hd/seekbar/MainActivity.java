package hd.seekbar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hdd.seekbar.DSeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DSeekBar dSeekBar = findViewById(R.id.activity_main_dsb_seek_bar);
        dSeekBar.setSeek(0.3544f);
    }
}
