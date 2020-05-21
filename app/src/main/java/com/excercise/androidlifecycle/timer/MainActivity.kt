package com.excercise.androidlifecycle.timer

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.TimeUtils
import com.excercise.androidlifecycle.R
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uptimeMillis = SystemClock.uptimeMillis() // counting time since boot. Will be stop
        val elapsedRealtime = SystemClock.elapsedRealtime()
        val elapsedRealtimeNanos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            SystemClock.elapsedRealtimeNanos()
        } else {
            0
        }
        val nanoTime = System.nanoTime()
        val currentTimeMillis = System.currentTimeMillis()
        val calendarTimeMillis = GregorianCalendar().timeInMillis

        println("uptimeMillis: $uptimeMillis. in time format: ${toTimeFormat(uptimeMillis)}")
        println("elapsed real time: $elapsedRealtime, in time format: ${toTimeFormat(elapsedRealtime)}")
        println("elapsed real time nanos: $elapsedRealtimeNanos, in time format: ${toTimeFormat(elapsedRealtimeNanos)}")
        println("nano time: $nanoTime")
        println("current time millis: $currentTimeMillis, in time format: ${toTimeFormat(currentTimeMillis)}")
        println("calendar time millis: $calendarTimeMillis, in time format: ${toTimeFormat(calendarTimeMillis)}")

        val elapsedRealTimeStart = SystemClock.elapsedRealtime()
        println("elapsed real time start: ${toTimeFormat(elapsedRealTimeStart)}")
        Thread.sleep(2000)
        val elapsedRealTimeEnd = SystemClock.elapsedRealtime()
        println("elapsed real time end: ${toTimeFormat(elapsedRealTimeEnd)}")
        println("interval time: ${(elapsedRealTimeEnd - elapsedRealTimeStart)/1000}")

        val nanoTimeStart = System.nanoTime()
        println("nano time start: $nanoTimeStart")
        Thread.sleep(2000)
        val nanoTimeEnd = System.nanoTime()
        println("nano time end: $nanoTimeEnd")
        println("interval time: ${TimeUnit.NANOSECONDS.toSeconds(nanoTimeEnd - nanoTimeStart)}")

        println("Calendar time zone")
        val calendar = GregorianCalendar()
        calendar.timeZone = TimeZone.getTimeZone("Etc/UTC")
        calendar.set(Calendar.HOUR_OF_DAY, 12)

        println("UTC: ${calendar.get(Calendar.HOUR_OF_DAY)}")
        println("UTC: ${calendar.timeInMillis}")

        calendar.timeZone = TimeZone.getTimeZone("Europe/Copenhagen")
        println("CPH: ${calendar.get(Calendar.HOUR_OF_DAY)}")
        println("CPH: ${calendar.timeInMillis}")

        calendar.timeZone = TimeZone.getTimeZone("America/New_York")
        println("NYC: ${calendar.get(Calendar.HOUR_OF_DAY)}")
        println("NYC: ${calendar.timeInMillis}")

        println("list of Timezone id: ")
        for (id in TimeZone.getAvailableIDs()) {
            println("id: $id")
        }

        println("Android Timer using chronometer")
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()


    }

    private fun toTimeFormat(millis: Long): String {
        val timeFormat = "dd/MM/yyyy HH:mm:ss"
        val ISOTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
        val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())
        return sdf.format(Date(millis))
    }


}
