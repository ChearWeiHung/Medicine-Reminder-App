package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackProgress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineproject.databinding.RvDateandtimeBinding

class DoctorTrackProgressRVAdaptor(private val dateAndTime: List<String>):
    RecyclerView.Adapter<DoctorTrackProgressRVAdaptor.ViewHolder>() {

    class ViewHolder(val binding: RvDateandtimeBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RvDateandtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dateAndTime[position]
        val dateAndTimeParts = currentItem.split(" ") //split date and time to two
        if (dateAndTimeParts.size == 2) {
            val datePart = dateAndTimeParts[0]  // "2024-11-07"
            val timePart = dateAndTimeParts[1]  // "14:30"

            currentItem.let {
                holder.binding.apply {
                    tvDateProgress.text = datePart
                    tvTimeProgress.text = timePart
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dateAndTime.size
    }
}