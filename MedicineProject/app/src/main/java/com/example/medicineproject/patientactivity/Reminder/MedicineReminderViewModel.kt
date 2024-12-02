import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MedicineReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance("https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")


    fun saveTimerState(remainingTime: Long, lastUpdatedTime: String, patientEmail: String, medicineName: String) {
        // Save Remaining countdown time and last update time to firebase
        val patientRef = firebaseRef.child(patientEmail).child("MedicineSchedule").child(medicineName)
        patientRef.child("remainingTimeMillis").setValue(remainingTime)
        patientRef.child("lastUpdatedTime").setValue(lastUpdatedTime)
    }
    // Calculate current remaining time based on saved remaining time and last updated time
    fun calculateCurrentRemainingTime(
        savedRemainingMillis: Long,
        savedLastUpdatedTime: String
    ): Long {
        if (savedLastUpdatedTime.isEmpty()) {
            // If the last updated time is empty, return the saved remaining time directly
            return savedRemainingMillis
        }

        // Parse the last updated time from the stored format
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val lastUpdatedTime = LocalDateTime.parse(savedLastUpdatedTime, formatter)

        // Calculate the time elapsed since the last update
        val now = LocalDateTime.now()
        val elapsedMillis = Duration.between(lastUpdatedTime, now).toMillis() // find the duration of last local time and now

        // Calculate the current remaining time by subtracting the elapsed time
        return savedRemainingMillis - elapsedMillis
    }
}

