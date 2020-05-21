package com.excercise.androidlifecycle.timer.countdown

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import com.excercise.androidlifecycle.R
import kotlinx.android.synthetic.main.activity_count_down_timer.*
import java.text.SimpleDateFormat
import java.util.*

class CountDownTimerActivity : AppCompatActivity() {

    val DURATION_MILLIS: Long = 61*1000
    val COUNT_DOWN_INTERVAL_MILLIS = 1000L
    var UJIAN_STATE_IDLE = "Ujian belum dimulai"
    var UJIAN_STATE_MULAI = "Ujian dimulai"
    var UJIAN_STATE_SELESAI = "Ujian selesai"

    lateinit var sharedPreferences: SharedPreferences
    val PREFERENCE_NAME = "COUNT_DOWN_PREFERENCE"
    val KEY_START_TIME = "START_TIME"

    lateinit var countDownTimer: CountDownTimer
    var isStopped = true
    var startTime: Long = 0
    var endTime: Long = 0
    var millisTillFinished: Long = 0
    var millisInFuture = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down_timer)

        sharedPreferences = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        
        btnTrigger.setOnClickListener{
            isStopped = if (isStopped) {
                startCountDown()
                sharedPreferences
                    .edit()
                    .putLong(KEY_START_TIME, startTime)
                    .apply()
                false
            } else {
                stopCountDown()
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()

        startTime = sharedPreferences.getLong(KEY_START_TIME, 0L)
        val currentTime = SystemClock.elapsedRealtime()
        millisInFuture = (DURATION_MILLIS - (currentTime - startTime))
        
        if (isUjianStillOnGoing(millisInFuture)) {
            startCountDown()
            isStopped = false
        } else {
            millisInFuture = DURATION_MILLIS
            isStopped = true
        }
    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
    }

    private fun startCountDown() {

        startTime = SystemClock.elapsedRealtime()
        countDownTimer = object: CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL_MILLIS) {
            override fun onFinish() {
                println("count down is finished")
                stopCountDown()
            }

            override fun onTick(millisUntilFinished: Long) {
                timer.text = toTimeFormat(millisUntilFinished)
                println("current time: ${toTimeFormat(millisUntilFinished)}")
                millisTillFinished = millisUntilFinished
            }
        }
        countDownTimer.start()
        onStartedViewState()
    }
    
    private fun stopCountDown() {
        endTime = SystemClock.elapsedRealtime()

        if (millisTillFinished > 0) {
            countDownTimer.cancel()
        }

        sharedPreferences
            .edit()
            .putLong(KEY_START_TIME, 0L)
            .apply()
        onStoppedViewState()
    }
    
    private fun isUjianStillOnGoing(timeRemaining: Long): Boolean {
        return timeRemaining > 0
    }

    private fun onStartedViewState() {
        durationTime.text = String.format("Duration: 00:00:00")
        durationTime2.text = String.format("Duration2: 00:00:00")
        tvStartTime.text = String.format("Start: ${toTimeFormat2(startTime)}")
        tvEndTime.text = String.format("End: 00:00:00")
        tvUjianMessage.text = UJIAN_STATE_MULAI
        btnTrigger.text = "STOP"
    }
    
    private fun onStoppedViewState() {
        durationTime.text = String.format("Duration: ${toTimeFormat2(endTime - startTime)}")
        durationTime2.text = String.format("Duration2: ${toTimeFormat(DURATION_MILLIS - millisTillFinished)}")
        tvEndTime.text = String.format("End: ${toTimeFormat2(endTime)}")
        tvUjianMessage.text = UJIAN_STATE_SELESAI
        btnTrigger.text = "START"
    }
    
    private fun toTimeFormat(millis: Long): String {
        val seconds = millis / 1000
        return String.format("%02d:%02d:%02d", (seconds / 3600), (seconds % 3600) / 60, (seconds % 60))
    }

    private fun toTimeFormat2(millis: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}
