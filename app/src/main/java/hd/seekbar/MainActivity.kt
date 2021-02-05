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

    private var isFocus = false
    private var i = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dSeekbar.setTotalDuration(60000)
            .setDSeekListener(object : DSeekListener {
                override fun onChange(duration: Long, totalDuration: Long, percent: Float, text: String, isFocus: Boolean) {
                    this@MainActivity.isFocus = isFocus
                    this@MainActivity.i = duration
                }

                override fun onError(error: String) {
                    Log.e("test", error)
                }
            })

        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    if (!isFocus) {
                        runOnUiThread {
                            dSeekbar.setDuration(i)
                        }
                        i = if (i < dSeekbar.getTotalDuration()) i + 10 else 0
                    }
                }
            },
            0,
            10
        )
    }
}