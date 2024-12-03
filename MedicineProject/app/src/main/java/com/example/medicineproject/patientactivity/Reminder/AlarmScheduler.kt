package com.example.medicineproject.patientactivity.Reminder

import android.content.Intent

interface AlarmScheduler {
    fun schedule(item: AlarmItem, alarmIntent: Intent)
    fun cancel(item: AlarmItem)
}