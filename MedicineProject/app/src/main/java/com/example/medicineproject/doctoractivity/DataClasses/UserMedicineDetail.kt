package com.example.medicineproject.doctoractivity.DataClasses

data class UserMedicineDetail(
    val medicineName: String?=null,
    val hours: String?=null,
    val pills: String?=null,
    val totalpills:String?=null,
    val pillsleft:String?=null,
    val date: String?=null,
    val remainingTimeMillis:Long?=null,
    val lastUpdatedTimes:String?=null,
    val alarmState:Boolean?=null
    )
