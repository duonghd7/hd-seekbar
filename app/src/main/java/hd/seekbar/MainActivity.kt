package hd.seekbar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hdd.seekbar.DSeekListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Timer
import java.util.TimerTask

/**
 * Create on 2/4/21
 * @author duonghd
 */

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dSeekbar.setTotalDuration(180000)
            .setDSeekListener(object : DSeekListener {
                override fun onChange(duration: Long, totalDuration: Long, percent: Float, text: String, isFocus: Boolean) {
                    Log.e("test", text)
                }

                override fun onError(error: String) {
                    Log.e("test", error)
                }
            })

        var i = 0L
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        dSeekbar.setDuration(i)
                    }
                    i = if (i < dSeekbar.getTotalDuration()) i + 10 else 0
                }
            },
            0,
            10
        )
    }
}