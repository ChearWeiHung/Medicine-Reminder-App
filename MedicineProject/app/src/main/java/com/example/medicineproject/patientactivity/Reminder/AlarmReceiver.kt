package com.example.medicineproject.patientactivity.Reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.medicineproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.TimeZone

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var firebaseRef: DatabaseReference
    private val databaseUrl = "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val channelID = "com.example.medicineproject.patientactivity.channel1"
    private var notificationManager: NotificationManager? = null

    companion object {
        const val ACTION_STOP_ALARM = "com.example.medicineproject.ACTION_STOP_ALARM"
        var playerMap: MutableMap<Int, MediaPlayer> = mutableMapOf() // Map to manage multiple alarms
        var isAlarmStoppedMap: MutableMap<Int, Boolean> = mutableMapOf() // Tracks stop state for each alarm
    }

    override fun onReceive(context: Context?, intent: Intent?) { //called when alarm goes off
        Log.d("AlarmReceiver", "onReceive called")

        if (context == null || intent == null) {
            return
        }

        val action = intent.action
        Log.d("AlarmReceiver", "onReceive called with action: $action")

        // Extract extras
        val patientEmail = intent.getStringExtra("PatientEmail") ?: return
        val medicineName = intent.getStringExtra("MedicineName") ?: return
        val pillLeft = intent.getStringExtra("PillLeft") ?: return
        val pillAmount = intent.getStringExtra("PillAmount") ?: return
        val date = intent.getStringExtra("Date") ?: return

        // Create a unique ID based on the medicine name hash
        val uniqueNotificationId = medicineName.hashCode()

        if (action == ACTION_STOP_ALARM) {
            // If the alarm has already been stopped, don't stop it again, ensuring that an alarm for a specific medicine can only be stopped once
            if (isAlarmStoppedMap[uniqueNotificationId] == true) { //check whether the specific alarm has been stopped based on the unique ID and state
                // If the alarm is already stopped, return and do nothing
                return
            }
            isAlarmStoppedMap[uniqueNotificationId] = true // Mark this alarm as stopped

            val firebaseRef =
                FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child(patientEmail)
            stopAlarm(uniqueNotificationId)
            val sharedPreferences = context.getSharedPreferences("MedicineReminderPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("$medicineName:Scheduled", false)
            editor.apply()

            // Get current date and time
            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("GMT+8")
            val currentDateTime = "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

            firebaseRef.child("MedicineSchedule").child(medicineName).child("pillsleft").get()
                .addOnSuccessListener { snapshot ->
                    val pillsLeft = snapshot.getValue(String::class.java)

                    if (pillsLeft != null) {
                        val pillsLeftInt = pillsLeft.toIntOrNull() ?: 0
                        val pillAmountInt = pillAmount.toIntOrNull() ?: 0
                        val updatedPillsLeft = (pillsLeftInt - pillAmountInt)

                        // Update the pillsLeft value in Firebase
                        firebaseRef.child("MedicineSchedule").child(medicineName).child("pillsleft")
                            .setValue(updatedPillsLeft.toString())
                        firebaseRef.child("date").child(date).child(medicineName).child("pillsleft")
                            .setValue(updatedPillsLeft.toString())
                        // Store data and time for doctor to track time
                        firebaseRef.child("DateAndTime").child(date).child(medicineName)
                            .child(currentDateTime).setValue(currentDateTime)
                        firebaseRef.child("MedicineSchedule").child(medicineName)
                            .child("alarmState").setValue(false)
                        checkPillLeftAndDelete(medicineName, patientEmail, updatedPillsLeft)
                    } else {
                        println("Pills left value is null or couldn't be retrieved")
                    }
                }
                .addOnFailureListener { error ->
                    println("Error retrieving pillsLeft: ${error.message}")
                }

        } else {
            // Reset the stop flag for the alarm
            isAlarmStoppedMap[uniqueNotificationId] = false

            startAlarm(context, uniqueNotificationId)
            val message = "Time to take your $medicineName"
            // Notification Services
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel()
            displayNotification(context, patientEmail, medicineName, pillLeft, pillAmount, date)
        }
    }

    private fun startAlarm(context: Context, uniqueNotificationId: Int) {
        if (!playerMap.containsKey(uniqueNotificationId)) { //first check if the map is already contain this id
            val player = MediaPlayer.create(context, R.raw.alarm2).apply { // no? creates a new MediaPlayer instance, starts it, and adds it to playerMap
                isLooping = true
                start() // Start the alarm sound
            }
            playerMap[uniqueNotificationId] = player  // Store the player in the map
            Log.d("AlarmReceiver", "Alarm started for ID: $uniqueNotificationId")
        }
    }

    private fun stopAlarm(uniqueNotificationId: Int) {
        Log.d("AlarmReceiver", "Stop Alarm called for ID: $uniqueNotificationId")

        playerMap[uniqueNotificationId]?.let {//When the alarm is stopped checks if the map contains the MediaPlayer
            if (it.isPlaying) {
                it.stop() // Stop the sound
                it.release() // Release the MediaPlayer resources
            }
            playerMap.remove(uniqueNotificationId) // Remove the player from the map
            Log.d("AlarmReceiver", "Alarm stopped and MediaPlayer released for ID: $uniqueNotificationId")
        }

        notificationManager?.cancel(uniqueNotificationId)
    }

    private fun displayNotification(
        context: Context,
        patientEmail: String,
        medicineName: String,
        pillLeft: String,
        pillAmount: String,
        date: String
    ) {
        // Create a unique ID for the notification based on the medicine name hash code
        // This ensures that each notification for a specific medicine has a unique ID
        val uniqueNotificationId = medicineName.hashCode()

        //Create an Intent that will be triggered when the user clicks "Stop Alarm" in the notification
        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_STOP_ALARM // send action when the stop action is triggered
            putExtra("PatientEmail", patientEmail)
            putExtra("MedicineName", medicineName)
            putExtra("PillLeft", pillLeft)
            putExtra("PillAmount", pillAmount)
            putExtra("Date", date)
        }
        // Create a PendingIntent that will be triggered when the "Stop Alarm" action is clicked
        val stopPendingIntent = PendingIntent.getBroadcast( //when triggered, send broadcast to alarm receiver
            context,
            uniqueNotificationId, // Use the unique ID for this pending intent
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelID) //build notification
            .setContentTitle("Reminder")
            .setContentText("Time to take your $medicineName")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_media_pause, "Stop Alarm", stopPendingIntent)// stop alarm action
            .setPriority(NotificationCompat.PRIORITY_HIGH) // set priority
            .build()

        // Notify using the unique ID to ensure this notification is not overridden
        notificationManager?.notify(uniqueNotificationId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelID, "Reminder Channel", importance).apply {
                description = "Channel for alarm reminders"
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun checkPillLeftAndDelete(medicineName: String, patientEmail: String, updatedPillsLeft: Int) {
        val firebaseRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child(patientEmail)

        if (updatedPillsLeft <= 0) {
            firebaseRef.child("MedicineSchedule").child(medicineName).removeValue()
        }
    }
}
