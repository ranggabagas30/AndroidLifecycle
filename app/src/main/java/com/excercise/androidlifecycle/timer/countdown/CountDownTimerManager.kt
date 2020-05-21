package com.excercise.androidlifecycle.timer.countdown

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class CountDownTimerManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val durationMillis: Long,
    private val countDownIntervalMillis: Long = 1000,
    private val listener: CountDownTimerListener
): LifecycleObserver {

    lateinit var countDownTimer: CountDownTimer
    var isStopped = true
    var startTime: Long = 0
    var endTime: Long = 0
    var elapsedTime = 0L
    var millisTillFinished: Long = 0

    var millisInFuture = 0L
    val PREFERENCE_NAME = "COUNT_DOWN_PREFERENCE"
    val KEY_START_TIME = "START_TIME"
    var sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    interface CountDownTimerListener {
        fun onStart()
        fun onFinish()
        fun onTick(millisUntilFinished: Long)
    }
    
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    /**
     *
     * ON RESUME
     *
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        startTime = sharedPreferences.getLong(KEY_START_TIME, 0L)
        val currentTime = SystemClock.elapsedRealtime()
        millisInFuture = (durationMillis - (currentTime - startTime))

        if (isThereTimeRemaining(millisInFuture)) {
            runCountDown()
        } else {
            millisInFuture = durationMillis
            elapsedTime = durationMillis
            isStopped = true
            listener.onFinish()
        }
    }

    /**
     * 
     * ON STOP
     * 
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) 
    fun stop() {
        countDownTimer.cancel()
    }
    
    private fun isThereTimeRemaining(timeRemaining: Long): Boolean {
        return timeRemaining > 0
    }

    fun startCountDown() {
        runCountDown()
        sharedPreferences
            .edit()
            .putLong(KEY_START_TIME, startTime)
            .apply()
    }
    
    fun runCountDown() {
        isStopped = false
        startTime = SystemClock.elapsedRealtime()
        countDownTimer = object: CountDownTimer(millisInFuture, countDownIntervalMillis) {
            override fun onFinish() {
                isStopped = true
                elapsedTime = durationMillis - millisTillFinished
                listener.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                millisTillFinished = millisUntilFinished
                listener.onTick(millisUntilFinished)
            }
        }
        countDownTimer.start()
        listener.onStart()
    }

    fun stopCountDown() {
        isStopped = true
        endTime = SystemClock.elapsedRealtime()

        if (millisTillFinished > 0) {
            countDownTimer.cancel()
        }

        sharedPreferences
            .edit()
            .putLong(KEY_START_TIME, 0L)
            .apply()
    }
}