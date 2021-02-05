# Android DSeekBar
[![](https://jitpack.io/v/duonghd7/hd-seekbar.svg)](https://jitpack.io/#duonghd7/hd-seekbar)<br>

## Introduction
**`DSeekBar`** is support for run custom seekbar, change time seekbar through interface **`DSeekListener()`**

## GIF
![giphy-dseekbar](https://user-images.githubusercontent.com/18477507/106988657-eb00e780-67a2-11eb-9e4c-cd9890c43ee3.gif)


## Installation
- Step 1. Add the JitPack repository to your build file <br>
`Add it in your root build.gradle at the end of repositories:`
```java
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

- Step 2. Add the dependency with lastest version [![](https://jitpack.io/v/duonghd7/hd-seekbar.svg)](https://jitpack.io/#duonghd7/hd-seekbar)<br>
```java
    dependencies {
          ...
          implementation 'com.github.duonghd7:hd-seekbar:0.0.4'
    }
```

## Options
![Screen Shot 2021-02-05 at 11 15 09](https://user-images.githubusercontent.com/18477507/106989172-218b3200-67a4-11eb-8b20-811b46c07501.png)<br>
![Screen Shot 2021-02-05 at 11 16 59](https://user-images.githubusercontent.com/18477507/106989175-23ed8c00-67a4-11eb-813a-f87dad941ea3.png)

## DSeekListener
**`.setDSeekListener(object : DSeekListener {})`** <br>

```java
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
```

## Notice
- You must set totalDuration first.

# MIT License

Copyright (c) 2021 Hà Đại Dương

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
