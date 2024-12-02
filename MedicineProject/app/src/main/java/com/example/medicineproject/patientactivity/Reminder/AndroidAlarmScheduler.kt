package com.example.medicineproject.patientactivity.Reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AndroidAlarmScheduler(private val context: Context) : AlarmScheduler {
    // Obtain the AlarmManager system service
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    // Create an Intent to broadcast the alarm and pass necessary data to AlarmReceiver
    override fun schedule(item: AlarmItem, alarmIntent: Intent) {
        // Use the unique id generated in AlarmItem
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("PatientEmail", item.patientEmail)
            putExtra("MedicineName", item.medicineName)
            putExtra("PillLeft", item.pillLeft)
            putExtra("PillAmount", item.pillAmount)
            putExtra("Date", item.date)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,  // Specifies that the alarm will trigger based on the real-time clock (RTC). It will wake up the device if it is in sleep mode or idle. This ensures the alarm can still fire even if the phone is not actively being used.
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,  //Scheduled Time in Milliseconds: This converts the scheduled time for the alarm (`item.time`), which is of type `LocalDateTime`, into milliseconds since the epoch (1970-01-01 00:00:00 UTC). The `ZoneId.systemDefault()` ensures that the system's default time zone is used for conversion.
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),  // Use a unique hash code generated from the AlarmItem's ID to create a distinct PendingIntent for each alarm
                intent,  // The Intent that will be triggered by the alarm, passing necessary data like the patient email, medicine name, etc.
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE  // Flags to update existing PendingIntent or make it immutable
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        // Use the unique id generated in AlarmItem
        alarmManager.cancel( // cancel the alarm
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),  // Use id's hashCode to cancel the correct PendingIntent
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
