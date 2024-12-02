package com.example.medicineproject.patientactivity.Reminder

import MedicineReminderViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.medicineproject.databinding.ActivityMedicineReminderBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class MedicineReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineReminderBinding
    private var alarmItem: AlarmItem? = null
    private lateinit var scheduler: AndroidAlarmScheduler
    private lateinit var firebaseRef: DatabaseReference
    private var remainingTimeMillis: Long = 0
    private var delete:Boolean = false
    private lateinit var viewModel: MedicineReminderViewModel
    private val databaseUrl =
        "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"

    // Declare the CountDownTimer at the class level
    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Ensure UI adjustments are needed here
        viewModel = ViewModelProvider(this).get(MedicineReminderViewModel::class.java)

        // Initialize scheduler
        scheduler = AndroidAlarmScheduler(this)

        // Retrieve Intent data
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
        val medicineName = intent.getStringExtra("MedicineName") ?: ""
        val medicineHours = intent.getStringExtra("MedicineHours") ?: ""
        val pills = intent.getStringExtra("PillAmount") ?: ""
        val pillsLeft = intent.getStringExtra("PillLeft") ?: ""
        val date = intent.getStringExtra("Date") ?: ""

        // Firebase initialization
        firebaseRef =
            FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child(patientEmail)
                .child("MedicineSchedule").child(medicineName)

        // Convert medicineHours to Long
        val medicineHoursLong: Long? = medicineHours.toLongOrNull()

        // Initialize ViewBinding
        binding = ActivityMedicineReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Display Information
        binding.tvMedicineNameReminder.text = "Medicine: $medicineName"
        binding.tvHoursReminder.text = "Time: $medicineHours Hours"
        binding.tvPill.text = "Pills: $pills pills"
        val sharedPrefs = getSharedPreferences("AlarmPrefs", MODE_PRIVATE)

// Retrieve the alarmItemId based on the current medicine
        val key = "alarmItemId_${medicineName}"  // Medicine-specific key
        val alarmItemId = sharedPrefs.getString(key, null)

        binding.backMedicineReminder.setOnClickListener {
            finish()
        }

        firebaseRef.get()
            .addOnSuccessListener { snapshot ->
                val alarmState = snapshot.child("alarmState").getValue(Boolean::class.java) ?: false
                Log.d("AlarmState", "Alarm State: $alarmState")

                if (alarmState == true) {
                    // Retrieve both remaining time and last updated time from Firebase
                    firebaseRef.get()
                        .addOnSuccessListener { snapshot ->
                            val savedRemainingMillis =
                                snapshot.child("remainingTimeMillis").getValue(Long::class.java)
                                    ?: 0L
                            val savedLastUpdatedTime =
                                snapshot.child("lastUpdatedTime").getValue(String::class.java)
                                    ?: ""

                            // Calculate the current remaining time
                            val currentRemainingTime = viewModel.calculateCurrentRemainingTime(
                                savedRemainingMillis,
                                savedLastUpdatedTime
                            )
                            binding.btnSchedule.isEnabled = false
                            binding.btnCancel.isEnabled = true

                            // Initialize and resume the countdown timer with the calculated remaining time
                            countdownTimer = object : CountDownTimer(currentRemainingTime, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    // Update remainingTimeMillis with the current millis until finish
                                    remainingTimeMillis = millisUntilFinished

                                    // Calculate hours, minutes, and seconds
                                    val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                                    val minutes =
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                            hours
                                        )
                                    val seconds =
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                            minutes
                                        ) - TimeUnit.HOURS.toSeconds(hours)

                                    // Format the remaining time as "HH:MM:SS"
                                    val formattedTime =
                                        String.format("%02d:%02d:%02d", hours, minutes, seconds)

                                    // Update the TextView to show the remaining time
                                    binding.tvTimer.text = "Time left: $formattedTime"
                                }

                                override fun onFinish() {
                                    // Handle timer finish
                                    binding.tvTimer.text = "Time's up!"
                                }
                            }

                            // Start the countdown timer
                            countdownTimer?.start()
                        }
                        .addOnFailureListener { exception ->
                            // Handle the error (e.g., log or show a message)
                            Log.e("FirebaseError", "Failed to fetch data", exception)
                        }
                } else {
                    binding.btnSchedule.isEnabled = true
                    binding.btnCancel.isEnabled = false
                }

            }


        binding.btnSchedule.setOnClickListener {
            // Prevent scheduling if the alarm is already active
            if (alarmItem == null) {
                val alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
                    action = AlarmReceiver.ACTION_STOP_ALARM
                    putExtra("MedicineName", medicineName)
                    putExtra("PillLeft", pillsLeft)
                    putExtra("Date", date)
                    putExtra("PatientEmail", patientEmail)
                    putExtra("PillAmount", pills)
                }
                if (medicineHoursLong != null) {

                    alarmItem = AlarmItem(
                        time = LocalDateTime.now().plusSeconds(medicineHoursLong),
                        patientEmail = patientEmail,
                        medicineName = medicineName,
                        pillLeft = pillsLeft,
                        pillAmount = pills,
                        date = date
                    )
                    //  // Create and start countdown timer
                    val countdownTime =
                        medicineHoursLong * 1000 // Convert seconds to milliseconds
                    countdownTimer = object : CountDownTimer(countdownTime, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            // Calculate hours, minutes, and seconds
                            remainingTimeMillis = millisUntilFinished
                            val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                            val minutes =
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    hours
                                )
                            val seconds =
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    minutes
                                ) - TimeUnit.HOURS.toSeconds(hours)

                            // Format the remaining time as "HH:MM:SS"
                            val formattedTime =
                                String.format("%02d:%02d:%02d", hours, minutes, seconds)

                            // Update the TextView to show the remaining time
                            binding.tvTimer.text = "Time left: $formattedTime"
                        }

                        override fun onFinish() {
                            // Do something when the timer finishes (if needed)
                        }
                    }

                    // Start the countdown
                    countdownTimer?.start()

                    alarmItem?.let { scheduler.schedule(it, alarmIntent) } // schedule alarm with data passed to alarm receiver
                    binding.btnSchedule.isEnabled = false
                    binding.btnCancel.isEnabled = true
                    firebaseRef.child("alarmState").setValue(true)
                    // Get SharedPreferences instance
                    val sharedPrefs = getSharedPreferences("AlarmPrefs", MODE_PRIVATE)

                    // Use the medicineName as part of the key
                    val key = "alarmItemId_$medicineName"
                    //use share preference since the alarm item disappear when activity is destroeyd
                    // Save the alarmItemId for this specific medicine
                    sharedPrefs.edit().putString(key, alarmItem?.id).apply()

                }
            }
        }


        // Cancel Alarm
        binding.btnCancel.setOnClickListener {
            // Retrieve the alarmItemId for the current medicine from SharedPreferences
            val sharedPrefs = getSharedPreferences("AlarmPrefs", MODE_PRIVATE)
            val medicineName = intent.getStringExtra("MedicineName") ?: ""
            val key = "alarmItemId_$medicineName"  // Medicine-specific key
            val alarmItemId = sharedPrefs.getString(key, null)

            // If alarmItemId exists, cancel the alarm associated with it
            if (alarmItemId != null) {
                // Fetch the alarmItem using the alarmItemId
                val alarmItemToCancel = AlarmItem(
                    id = alarmItemId,
                    time = LocalDateTime.now().plusSeconds(8),
                    patientEmail = patientEmail,
                    medicineName = medicineName,
                    pillLeft = pillsLeft,
                    pillAmount = pills,
                    date = date
                )
                scheduler.cancel(alarmItemToCancel)  // Cancel the alarm using the retrieved ID
            }

            // Cancel the countdown timer if it is running
            countdownTimer?.cancel()  // Stop the countdown timer
            countdownTimer = null // Reset the countdownTimer

            // Reset UI and state
            alarmItem = null  // Reset alarmItem
            binding.btnSchedule.isEnabled = true
            binding.btnCancel.isEnabled = false

            // Update Firebase to reflect the cancellation of the alarm
            firebaseRef.child("alarmState").setValue(false)

            //remove the saved alarmId from SharedPreferences if it's no longer needed
            sharedPrefs.edit().remove(key).apply()
        }


        binding.btnDelete.setOnClickListener {
            // Cancel the alarm if it exists
            val sharedPrefs = getSharedPreferences("AlarmPrefs", MODE_PRIVATE)
            val medicineName = intent.getStringExtra("MedicineName") ?: ""
            val key = "alarmItemId_$medicineName"  // Medicine-specific key
            val alarmItemId = sharedPrefs.getString(key, null)

            // If alarmItemId exists, cancel the alarm associated with it
            if (alarmItemId != null) {
                // Create an AlarmItem using only the alarmItemId
                val alarmItemToCancel = AlarmItem(
                    id = alarmItemId,
                    time = LocalDateTime.now().plusSeconds(8),
                    patientEmail = patientEmail,
                    medicineName = medicineName,
                    pillLeft = pillsLeft,
                    pillAmount = pills,
                    date = date
                )  // Only pass the ID to cancel
                scheduler.cancel(alarmItemToCancel)  // Cancel the alarm using the ID

            }

            // Reset countdown timer if it is running
            countdownTimer?.cancel()  // Stop the countdown timer
            countdownTimer = null // Reset the countdownTimer

            // Reset alarmItem to null
            alarmItem = null

            // Remove the alarmItemId from SharedPreferences
            sharedPrefs.edit().remove(key).apply()

            // Remove the alarm data from Firebase
            firebaseRef.removeValue()
            delete=true

            // Finish the activity
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        val sharedPrefs = getSharedPreferences("AlarmPrefs", MODE_PRIVATE)

        // Retrieve the alarmItemId based on the current medicine
        val medicineName = intent.getStringExtra("MedicineName") ?: ""
        val key = "alarmItemId_${medicineName}"  // Medicine-specific key
        val alarmItemId = sharedPrefs.getString(key, null)
        // Check if the alarm is still running and update the UI
        if (alarmItem != null) {
            binding.btnSchedule.isEnabled = false // Disable schedule button if alarm is running
            binding.btnCancel.isEnabled = true  // Enable cancel button if alarm is running
        } else {
            binding.btnSchedule.isEnabled =
                true // Enable schedule button if no alarm is running
            binding.btnCancel.isEnabled = false  // Disable cancel button if no alarm is running
        }
    }

    override fun onPause() {

        val medicineName = intent.getStringExtra("MedicineName") ?: ""
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
        super.onPause()
        if (delete==false) { //if this medicine is deleted, no need to save the state to firebase

            viewModel.saveTimerState(
                remainingTimeMillis,
                LocalDateTime.now().toString(),
                patientEmail ,
                medicineName ,
            )
        }

    }

    override fun onDestroy() {
        val medicineName = intent.getStringExtra("MedicineName") ?: ""
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
        super.onDestroy()
        if (delete==false) { //if this medicine is deleted, no need to save the state to firebase

            viewModel.saveTimerState(
                remainingTimeMillis,
                LocalDateTime.now().toString(),
                patientEmail ,
                medicineName ,
            )
        }

    }
}