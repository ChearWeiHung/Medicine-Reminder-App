package com.example.medicineproject.patientactivity.Reminder

import java.time.LocalDateTime
import java.util.UUID

data class AlarmItem(
    val time: LocalDateTime,
    val patientEmail: String,
    val medicineName: String,
    val pillLeft: String,
    val pillAmount: String,
    val date: String,
    val id: String = UUID.randomUUID().toString()

)
