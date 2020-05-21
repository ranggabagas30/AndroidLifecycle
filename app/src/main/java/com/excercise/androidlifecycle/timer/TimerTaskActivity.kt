package com.excercise.androidlifecycle.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.excercise.androidlifecycle.R
import java.util.*

class TimerTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_task)

        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                println("this line is executed")
            }
        }

        timer.schedule(timerTask, 1000, 1)
    }
}
