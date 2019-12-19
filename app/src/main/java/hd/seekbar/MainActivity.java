package hd.seekbar;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hdd.seekbar.DSeekBar;

public class MainActivity extends AppCompatActivity {

    private DSeekBar dSeekBar;
    private Handler handler = new Handler();
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dSeekBar = findViewById(R.id.activity_main_dsb_seek_bar);
        dSeekBar.setTotalDuration(180000)
                .setDSeekListener(new DSeekBar.DSeekListener() {
                    @Override
                    public void onChange(int duration, int totalDuration, float percent, String text) {
                        i = duration;
                        Log.e("test", text);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("test", error);
                    }
                });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dSeekBar.setDuration(i);
                i = i < dSeekBar.getTotalDuration() ? i + 100 : 0;
                handler.postDelayed(this, 100);
            }
        };

        handler.post(runnable);
    }
}
