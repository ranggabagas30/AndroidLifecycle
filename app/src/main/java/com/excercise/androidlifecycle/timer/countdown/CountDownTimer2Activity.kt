package com.excercise.androidlifecycle.timer.countdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.excercise.androidlifecycle.R
import com.excercise.androidlifecycle.timer.countdown.TimeUtil.toTimeFormat
import com.excercise.androidlifecycle.timer.countdown.TimeUtil.toTimeFormat2
import kotlinx.android.synthetic.main.activity_count_down_timer.*
import java.util.*

class CountDownTimer2Activity : AppCompatActivity() {

    private val DURATION_MILLIS: Long = 10 * 1000 + 100
    var millisTillFinished = 0L
    val UJIAN_STATE_MULAI = "Ujian dimulai"
    val UJIAN_STATE_SELESAI = "Ujian selesai"
    lateinit var countDownTimerManager: CountDownTimerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down_timer)

        countDownTimerManager = CountDownTimerManager(
            this,
            this,
            DURATION_MILLIS,
            listener = object : CountDownTimerManager.CountDownTimerListener {

                override fun onStart() {
                    onStartedViewState()
                }

                override fun onFinish() {
                    onStoppedViewState()
                }

                override fun onTick(millisUntilFinished: Long) {
                    millisTillFinished = millisUntilFinished
                    onRunningViewState()
                }
            }
        )

        btnTrigger.setOnClickListener {
            if (countDownTimerManager.isStopped) {
                countDownTimerManager.startCountDown()
                onStartedViewState()
            } else {
                countDownTimerManager.stopCountDown()
                onStoppedViewState()
            }
        }
    }

    private fun onRunningViewState() {
        println("current time: $millisTillFinished(${toTimeFormat(millisTillFinished)})")
        timer.text = toTimeFormat(millisTillFinished)
    }

    private fun onStartedViewState() {
        durationTime2.text = String.format("Duration2: ${toTimeFormat(countDownTimerManager.elapsedTime)}")
        tvStartTime.text = String.format("Start: ${toTimeFormat2(countDownTimerManager.startTime)}")
        tvEndTime.text = String.format("End: ${toTimeFormat2(countDownTimerManager.endTime)}")
        tvUjianMessage.text = UJIAN_STATE_MULAI
        btnTrigger.text = "STOP"
    }

    private fun onStoppedViewState() {
        println("count down is finished with millisTillFinished: $millisTillFinished")
        durationTime2.text = String.format("Duration2: ${toTimeFormat(countDownTimerManager.elapsedTime)}")
        tvStartTime.text = String.format("Start: ${toTimeFormat2(countDownTimerManager.startTime)}")
        tvEndTime.text = String.format("End: ${toTimeFormat2(countDownTimerManager.endTime)}")
        tvUjianMessage.text = UJIAN_STATE_SELESAI
        btnTrigger.text = "START"
    }
}
